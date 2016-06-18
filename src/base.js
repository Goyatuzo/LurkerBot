"use strict";

var Discord = require('discord.js');
var bot = new Discord.Client({ queue : true });

var initialFunction = require('./Handlers/initial');
var words = require('../src/counter');

// Run the init function to set up the words dictionary.
bot.on('ready', function() {
    initialFunction(bot);
});

bot.loginWithToken(process.env.DISCORD_TOKEN);