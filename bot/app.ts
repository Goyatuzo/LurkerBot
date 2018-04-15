import * as Discord from "discord.js";

import presenceEvent from "./handlers/events/presence";
import messageEvent from "./handlers/events/message";
import readyEvent from './handlers/events/ready';

import { Configuration } from "./helpers/environment";
import { connectionOptions } from '../typeorm'

import { createConnection } from 'typeorm';

/**
 * BOT token = process.env.DISCORD_TOKEN
 * DB URL = process.env.LURKER_DB
 * DB Username = process.env.LURKER_USERNAME
 * DB Password = process.env.LURKER_PASSWORD
 * DB Default = process.env.LURKER_SCHEMA
 */
//Startup.init();
createConnection(connectionOptions).then(conn => {
    console.log("Typeorm connected to database.");

    var bot = new Discord.Client();

    bot.on('ready', () => readyEvent(bot));
    // bot.on('presenceUpdate', presenceEvent);
    // bot.on('message', messageEvent);

    bot.login(Configuration.DISCORD_TOKEN);
}).catch(err => {
    console.error(err);
})
