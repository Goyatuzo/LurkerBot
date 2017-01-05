import * as Discord from "discord.js";
import * as _ from "lodash";

import { createConnection } from './models';

import readyEvent from './handlers/events/ready';
import presenceEvent from "./handlers/events/presence";
import messageEvent from "./handlers/events/message";

/**
 * BOT token = process.env.DISCORD_TOKEN
 * DB URL = process.env.LURKER_DB
 * DB Username = process.env.LURKER_USERNAME
 * DB Password = process.env.LURKER_PASSWORD
 * DB Default = process.env.LURKER_SCHEMA
 */

var bot = new Discord.Client();

createConnection().then(connection => {
    console.log("DB synced.");

    bot.login(process.env.DISCORD_TOKEN);

    // 30 minutes
    const deleteInterval = 1000 * 60 * 30;

    bot.on('ready', readyEvent(bot));

    bot.on('presenceUpdate', presenceEvent);
    bot.on('message', messageEvent);
}).catch(err => console.error(err));