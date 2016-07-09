var GameStats = require('../../tools/gamestats');
var Logger = require('../../logger');
var UserMethods = require('../../tools/user_methods');

function stringifyTime(seconds) {
    var m = Math.floor(seconds / 60);
    var seconds = seconds % 60;

    var h = Math.floor(m / 60);
    var minutes = m % 60;

    var days = Math.floor(h / 24);
    var hours = Math.floor(h % 24);
    return days + ' days ' + hours + ' hours ' + minutes + ' minutes ' + seconds + ' seconds';
}

function statsString(stats) {
    var summaryString = "";
    for (var game in stats) {
        summaryString += `${game}\n - ${stringifyTime(stats[game])}\n`;
    }

    return summaryString;
}


/**
 * Print the summary of all the stats of the users contained in the users array.
 */
function printStatsSummary(client, channel, users) {
    var userIds = users.map(function (user) {
        return user.id;
    });

    GameStats.getSummary(userIds, function (error, result) {
        if (error) {
            console.log(error);
            return;
        }

        var resultString = '';
        var game;

        for (var i = 0; i < result.length; ++i) {
            game = result[i];

            resultString += `${game.GAMENAME}\n - ${stringifyTime(game.DURATION)}\n\n`;
        }

        client.sendMessage(channel, resultString);
    });
}

/**
 * Print the stats of a single user.
 */
function printUserStats(client, channel, user) {
    Logger.log(`${UserMethods.getUniqueName(user)} requested STATS summary.`)

    var userId = user.id;

    GameStats.getStatsFor(userId, function (error, result) {
        if (error) {
            console.log(error);
            return;
        }

        var resultString = '';
        var game;

        for (var i = 0; i < result.length; ++i) {
            game = result[i];

            resultString += `${game.GAMENAME}\n - ${stringifyTime(game.DURATION)}\n\n`;
        }

        client.sendMessage(channel, resultString);
    });
}

module.exports = function (client, message) {
    var server = message.channel.server;

    var messageString = message.cleanContent;

    // Get the arguments of stats.
    var tokens = messageString.split(' ');

    // Game and Name.
    var type = tokens[1];
    var name = tokens[2];

    // If there is no type, then print the summary for the server.
    if (type === undefined) {
        // Put the member listing in an array.
        var users = (server.members).map(function (idx) {
            return idx;
        });

        Logger.log('Server STATS summary requested.');
        printStatsSummary(client, message.channel, users);
    } else if (type === 'me') {
        var user = message.author;

        printUserStats(client, message.channel, user);
    }

}
