"use strict";

var Logger = require('../logger');
var puppyHandler = require('./message/puppy');

module.exports = function(message) {
    var messageString = message.cleanContent;

    if (messageString === 'give me a puppy') {
        puppyHandler(message);
    }
}
