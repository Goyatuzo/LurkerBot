"use strict";

var Discord = require('discord.js');
var bot = new Discord.Client({ queue : true });

var initialFunction = require('./Handlers/initial');
var words = require('../src/counter');

// Run the init function to set up the words dictionary.
bot.on('ready', function() {
    initialFunction(bot);
});

bot.on('presence', function(before, after) {
    console.log(before.name + ' has changed something about his status.');
});

bot.on('message', function(message) {
    console.log(message.cleanContent);
})

bot.loginWithToken(process.env.DISCORD_TOKEN);