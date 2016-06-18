"use strict";

var words = require('../counter');

function countWords(messages) {
    console.log(messages.length);
    for (var i = 0; i < messages.length; ++i) {
        // Split message into tokens and count the words.
        var tokens = messages[i].split(' ');
        for (var j = 0; j < tokens.length; ++j) {
            words.count(tokens[j]);
        }

    }

    console.log(words.current);
}

function messageHandler(client) {
    var textChannel = client.channels.get("name", "us-branch");
    
    // Why is this limited to 100 messages?
    client.getChannelLogs(textChannel, 1000, function(err, messages) {
        var messageStrings = messages.map(function(message) {
            return message.cleanContent;
        });

        countWords(messageStrings);
    });
}

module.exports = messageHandler;