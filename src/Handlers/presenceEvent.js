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

    GameStats.addGame(uniqueName, gameName);
}

/**
 * Call when logging ends for a user playing a game.
 */
function endLogging(uniqueName, id, gameName) {
    Logger.log('Logging has ended for ' + uniqueName + ' playing ' + gameName);

    var seconds = GameStats.getTime(uniqueName, gameName);

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
    var game;
    // If the game is on the before state, it has been quit.
    if (UserMethods.getGame(before)) {
        game = UserMethods.getGame(before);
        Logger.log(name + ' has quit ' + game);
    }

    // If a game has been quit, have it quit logging for that user.
    if (game) {
        // Since stat is async, create temp so value is stored.
        var tempGame = game;

        // Make sure the user file exists. If not, create it.
        fs.stat(GameStats.filePathFromName(id, tempGame), (err, res) => {
            // If no err
            if (err) {
                if (err.code === 'ENOENT') {
                    fs.writeFileSync(GameStats.filePathFromName(id, tempGame), '{}');
                }
            }

            endLogging(name, id, tempGame);
        });
    }

    game = undefined;
    if (UserMethods.getGame(after)) {
        game = UserMethods.getGame(after);
        Logger.log(name + ' has begun playing ' + game);
    }

    if (game) {
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
