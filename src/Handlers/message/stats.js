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

    GameStats.getSummary(userIds, function (error, result) {
        if (error) {
            console.log(error);
            return;
        }

        var resultString = '';

        for (var i = 0; i < result.length; ++i) {
            var game = result[i];

            resultString += `${game.GAMENAME}\n - ${stringifyTime(game.DURATION)}\n\n`;
        }

        client.sendMessage(channel, resultString);
    });
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
