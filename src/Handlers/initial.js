"use strict";

var words = require('../counter');

function messageHandler(client) {
    var textChannel = client.channels.get("name", "us-branch");
    
    client.getChannelLogs(textChannel, 10000, function(err, messages) {
        var messageStrings = messages.map(function(message) {
            return message.cleanContent;
        });
    });
}

module.exports = messageHandler;