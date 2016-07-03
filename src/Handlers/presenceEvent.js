"use strict";

var fs = require('fs');

var UserMethods = require('../tools/user_methods');
var GameStats = require('../tools/gamestats');

var Logger = require('../logger');

var mkdirp = require('mkdirp');

// Make sure the folder to place log files exists.
mkdirp(GameStats.folderPath);

/**
 * Call when logging begins for a user playing a game.
 */
function beginLogging(uniqueName, id, gameName) {
    Logger.log('Logging has begun for ' + uniqueName + ' playing ' + gameName);

    GameStats.addGame(id, gameName);
}

/**
 * Call when logging ends for a user playing a game.
 */
function endLogging(uniqueName, id, gameName) {
    Logger.log('Logging has ended for ' + uniqueName + ' playing ' + gameName);

    var seconds = GameStats.getTime(id, gameName);
    GameStats.removeGame(id, gameName);

    // If undefined seconds, just exit and don't do anything.
    if (seconds === undefined || seconds === null) {
        Logger.warn('Seconds is ' + seconds);
        return;
    }

    GameStats.writeData(id, gameName, seconds);
}

/**
 * Main function of the stats tracking feature of the bot. It assumes that if the user in the before state
 * has a game, they are quitting that game. If a user in the after state has a game, they are just entering
 * the game.
 */
function gameTracker(before, after) {
    var name = UserMethods.getUniqueName(before);
    var id = UserMethods.getId(before);
    var game = UserMethods.getGame(before);

    // If the BEFORE state has a game, it means the game is being ended one way or anothoer.
    if (game) {
        Logger.log(name + ' has quit ' + game);
        endLogging(name, id, game);
    }

    game = UserMethods.getGame(after);

    // If the AFTER state has a game, it means the game is being started.
    if (game) {
        Logger.log(name + ' has begun ' + game);
        beginLogging(name, id, game);
    }
}

/**
 * The actual function that processes each "presence" event fired.
 */
module.exports = function (before, after) {
    var name = UserMethods.getUniqueName(before);

    // Validation to make sure it's the same user whose presence has been logged.
    var sameUser = name === UserMethods.getUniqueName(after);

    if (sameUser) {
        gameTracker(before, after);
    }
}
