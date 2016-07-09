"use strict";

var Logger = require('../../logger');

var puppyHandler = require('../message/puppy');
var statsHandler = require('../message/stats');

module.exports = function (client) {
    return function (message) {
        var messageString = message.cleanContent;

        if (messageString === 'give me a puppy') {
            puppyHandler(message);
        }

        if (messageString.startsWith('stats')) {
            statsHandler(client, message);
        }
    }
}
