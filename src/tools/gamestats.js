"use strict";

var fs = require('fs');
var Logger = require('../logger');
var mysql = require('mysql2');

var connection = mysql.createConnection({
    host: process.env.GAMEDB,
    port: 3306,
    user: process.env.USERNAME,
    password: process.env.PASSWORD,
    database: process.env.DBNAME
});

function _connect(conn) {
    // Connect to DB and define the Times table here.
    conn.connect(function (err) {
        if (err) {
            Logger.warn(err);
            return;
        }

        connection.execute(`
        CREATE TABLE IF NOT EXISTS Times (
            id          VARCHAR(25) NOT NULL,
            endTime     DATETIME    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
            gameName    VARCHAR(45) NOT NULL,
            duration    INT(6)      NOT NULL
        )
    `);
    });
}

_connect(connection);

connection.on('error', function (err) {
    console.log(err);

    _connect(connection);
})


var stats = {};

class Timer {
    constructor() {
        // Start counting the time.
        this.start = new Date();
    }

    timePlayed() {
        var curr = new Date();

        return Math.floor((curr - this.start) / 1000);
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
    Logger.log(`${gameName} is now being tracked.`);
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
        Logger.log(`${gameName} is not being tracked anymore.`);
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
            console.log(`${userId}.txt couldn't be found.`);
            callback({});
            return;
        }

        var stats = JSON.parse(data);

        callback(stats);
    });
}

/**
 * Given the stats input to the function, write data that is the string represntation of the JSON input.
 */
function writeData(userId, gameName, time) {
    connection.execute(`INSERT INTO Times (id, gameName, duration) VALUES (?, ?, ?)`, [userId, gameName, time], function (err) {
        if (err) {
            Logger.warn(err);
            return;
        }

        Logger.log(`Saving stats for ${userId} playing ${gameName} for ${time}.`)
    });
}

/**
 * Get the summary (total of all games played) by the players in the server.
 */
function getSummary(ids, callback) {
    // Convert ids array to string to be parsed by sqlite3.
    var idString = JSON.stringify(ids);

    // Regex to replace brackets with nothing.
    idString = idString.replace(/[\[\]']+/g, '');

    connection.execute(`SELECT gameName, SUM(duration) AS duration FROM lurkerbot.Times WHERE ID in (${idString}) GROUP BY gameName`, function (err, rows) {
        if (err) {
            Logger.warn(err);
        }

        callback(err, rows);
    });
}

function getStatsFor(id, callback) {
    connection.execute(`SELECT gameName, SUM(duration) AS duration FROM Times WHERE ID=(?) GROUP BY gameName`, [id], function (err, rows) {
        if (err) {
            Logger.warn(err);
        }

        callback(err, rows);
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

    writeData: writeData,

    getSummary: getSummary,
    getStatsFor: getStatsFor
};
