"use strict";

var Logger = require('../../logger');
var randomPuppy = require('random-puppy');

module.exports = function (message) {
    var channel = message.channel;

    Logger.log('Puppy requested.');

    randomPuppy().then(url => {
        message.client.sendMessage(channel, url);
    });
}
