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
function _gameExists(uniqueName, gameName) {
    if (uniqueName in stats) {
        if (gameName in stats[uniqueName]) {
            return true;
        }
    }

    // If the name or the game doesn't exist, return false.
    return false;
}

/**
 * Add a game to the user's list of game times being tracked.
 */
function addGame(uniqueName, gameName) {
    // If the name already exists, check to see if the game does.
    if (uniqueName in stats) {
        stats[uniqueName][gameName] = new Timer();
    } else {
        stats[uniqueName] = {};
        stats[uniqueName][gameName] = new Timer();
    }
}

/**
 * If the game exists in the stats dictionary, remove it. Otherwise do nothing. 
 */
function removeGame(uniqueName, gameName) {
    // Find the TimeData object corresponding to the gameName.
    if (_gameExists(uniqueName, gameName)) {
        delete stats[uniqueName][gameName];
    }
}

/**
 *  Get the time played from the user and name of the game. If no suitable game, return undefined.
 */
function getTime(uniqueName, gameName) {
    if (_gameExists(uniqueName, gameName)) {
        return stats[uniqueName][gameName].timePlayed();
    }

    return undefined;
};

/**
 * Get a dictionary of the times associated with each game from the input user name.
 */
function getTimes(uniqueName) {
    // If the user DNE, return undefined.
    if (!(uniqueName in stats)) {
        return undefined;
    }

    var gameList = stats[uniqueName];

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
function filePathFromName(uniqueName) {
    return folderPath + uniqueName + '.txt';
}

/**
 * Read in the data that exists for a particular user. This function assumes that the file associated
 * with the user already exists.
 */
function getExistingTimes(uniqueName, callback) {
    fs.readFile(filePathFromName(uniqueName), (error, data) => {
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
function writeData(uniqueName, stats) {
    Logger.log('////////////////////////////////////////////');
    Logger.log('Currently stored on memory:');
    Logger.log(stats);

    for (var key in stats) {
        Logger.log(key + ": " + stats[key]);
    }

    Logger.log('////////////////////////////////////////////');
    Logger.log('To be stored on disk:');
    Logger.log(JSON.stringify(stats));
    Logger.log('////////////////////////////////////////////');

    

    fs.writeFile(filePathFromName(uniqueName), JSON.stringify(stats), (error) => {
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