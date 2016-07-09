"use strict";

var Discord = require('discord.js');
var bot = new Discord.Client({ queue: true });

var presenceHandler = require('./Handlers/presenceEvent');
var messageHandler = require('./Handlers/messageEvent');

var Logger = require('./logger.js');

bot.on('ready', function () {
    // When someone's status changes, run a function.
    bot.on('presence', presenceHandler);
    bot.on('message', messageHandler(bot));

    Logger.log(bot.servers);
});

bot.loginWithToken(process.env.DISCORD_TOKEN);
