import * as Discord from "discord.js";
import * as _ from "lodash";

import presenceEvent from "./handlers/events/presence";
import messageEvent from "./handlers/events/message";
import readyEvent from './handlers/events/ready';

import Startup from './helpers/startup';

/**
 * BOT token = process.env.DISCORD_TOKEN
 * DB URL = process.env.LURKER_DB
 * DB Username = process.env.LURKER_USERNAME
 * DB Password = process.env.LURKER_PASSWORD
 * DB Default = process.env.LURKER_SCHEMA
 */
Startup.runInitialCheck();
Startup.init();

var bot = new Discord.Client();

bot.on('ready', () => readyEvent(bot));
bot.on('presenceUpdate', presenceEvent);
bot.on('message', messageEvent);

bot.login(process.env.DISCORD_TOKEN);
