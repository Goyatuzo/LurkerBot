var GameStats = require('../../tools/gamestats');
var Logger = require('../../logger');

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

function printStatsSummary(client, channel, users) {
    var summary = {};

    var userIds = users.map(function (user) {
        return user.id;
    });

    for (var i = 0; i < userIds.length; ++i) {
        var count = 0;

        GameStats.getExistingTimes(userIds[i], function (stats) {

            for (var game in stats) {
                if (game in summary) {
                    summary[game] += stats[game];
                } else {
                    summary[game] = stats[game];
                }
            }

            // If on the final userId, print out the summary.
            if (count === userIds.length - 1) {
                var summaryString = statsString(summary);
                Logger.log(`Printing stats summary to: ${channel.name}`);
                client.sendMessage(channel, summaryString);
            }

            count++;
        });
    }
}

module.exports = function (client, message) {
    var server = message.channel.server;

    // Put the member listing in an array.
    var users = (server.members).map(function (idx) {
        return idx;
    });

    Logger.log('Server stats summary requested.');
    printStatsSummary(client, message.channel, users);
}