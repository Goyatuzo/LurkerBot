"use strict";

var fs = require('fs');
var Logger = require('../logger');

var stats = {};

class Timer {
    constructor() {
        // Start counting the time.
        this.start = new Date();
    }

    timePlayed() {
        var curr = new Date();

        return Math.floor((curr - this.start) / 1000 );
    }
}

/**
 * Helper functon to help determine whether or not the game exists in the stats dictionary.
 */
function _gameExists(userId, gameName) {
    if (userId in stats) {
        if (gameName in stats[userId]) {
            return true;
        }
    }

    // If the name or the game doesn't exist, return false.
    return false;
}

/**
 * Add a game to the user's list of game times being tracked.
 */
function addGame(userId, gameName) {
    // If the name already exists, check to see if the game does.
    if (userId in stats) {
        // Since bot will be running in multiple servers, make sure it doesn't count the same game twice.
        if (!(gameName in stats[userId])) {
            stats[userId][gameName] = new Timer();
        } else {
            console.log("Duplicate game being tracked.");
        }
    } else {
        stats[userId] = {};
        stats[userId][gameName] = new Timer();
    }
}

/**
 * If the game exists in the stats dictionary, remove it. Otherwise do nothing. 
 */
function removeGame(userId, gameName) {
    // Find the TimeData object corresponding to the gameName.
    if (_gameExists(userId, gameName)) {
        delete stats[userId][gameName];
    }
}

/**
 *  Get the time played from the user and name of the game. If no suitable game, return undefined.
 */
function getTime(userId, gameName) {
    if (_gameExists(userId, gameName)) {
        return stats[userId][gameName].timePlayed();
    }

    return undefined;
};

/**
 * Get a dictionary of the times associated with each game from the input user name.
 */
function getTimes(userId) {
    // If the user DNE, return undefined.
    if (!(userId in stats)) {
        return undefined;
    }

    var gameList = stats[userId];

    var dict = {};

    // Iterate through the keys and add the time played to the dictionary to be returned. 
    for (var key in gameList) {
        dict[key] = gameList[key].timePlayed();
    }

    return dict;
}


var folderPath = 'resources/stats/';
/**
 * Helper function to get the filepath to the username.
 */
function filePathFromName(userId) {
    return folderPath + userId + '.txt';
}

/**
 * Read in the data that exists for a particular user. This function assumes that the file associated
 * with the user already exists.
 */
function getExistingTimes(userId, callback) {
    fs.readFile(filePathFromName(userId), (error, data) => {
        if (error) {
            console.log("File name couldn't be found.");
            return;
        }

        var stats = JSON.parse(data);

        callback(stats);
    });
}

/**
 * Given the stats input to the function, write data that is the string represntation of the JSON input.
 */
function writeData(userId, stats) {
    Logger.log(`Saving stats for ${userId}.`);

    fs.writeFile(filePathFromName(userId), JSON.stringify(stats), (error) => {
        if (error) throw error;
    });
}

module.exports = {
    addGame: addGame,

    folderPath: folderPath,
    filePathFromName: filePathFromName,

    getExistingTimes: getExistingTimes,

    getTimes: getTimes,
    getTime: getTime,

    removeGame: removeGame,

    writeData: writeData
};