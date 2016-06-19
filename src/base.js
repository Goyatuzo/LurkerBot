"use strict";

var Discord = require('discord.js');
var bot = new Discord.Client({ queue : true });

var presenceHandler = require('./Handlers/presence');

var words = require('../src/counter');

// When someone's status changes, run a function.
bot.on('presence', presenceHandler);


bot.loginWithToken(process.env.DISCORD_TOKEN);