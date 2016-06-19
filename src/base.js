"use strict";

var Discord = require('discord.js');
var bot = new Discord.Client({ queue : true });

var initialHandler = require('./Handlers/initial');
var presenceHandler = require('./Handlers/presence');

var words = require('../src/counter');

// Run the init function to set up the words dictionary.
bot.on('ready', function() {
    initialHandler(bot);
});

// When someone's status changes, run a function.
bot.on('presence', presenceHandler);


bot.loginWithToken(process.env.DISCORD_TOKEN);