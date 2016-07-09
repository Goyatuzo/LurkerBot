"use strict";

var Discord = require('discord.js');
var bot = new Discord.Client({ queue: true });

var presenceHandler = require('./Handlers/event/presenceEvent');
var messageHandler = require('./Handlers/event/messageEvent');

bot.on('ready', function () {
    // When someone's status changes, run a function.
    bot.on('presence', presenceHandler);
    bot.on('message', messageHandler(bot));

});

bot.loginWithToken(process.env.DISCORD_TOKEN);