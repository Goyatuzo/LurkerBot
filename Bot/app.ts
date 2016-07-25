import * as Discord from "discord.js";
import presenceEvent from "./handlers/events/presence";
import * as _ from "lodash";

/**
 * BOT token = process.env.DISCORD_TOKEN
 * DB URL = process.env.LURKER_DB
 * DB Username = process.env.LURKER_USERNAME
 * DB Password = process.env.LURKER_PASSWORD
 * DB Default = process.env.LURKER_SCHEMA
 */

var bot = new Discord.Client();
bot.loginWithToken(process.env.DISCORD_TOKEN);

bot.on('ready', event => {
    const serverNames = _.map(bot.servers, server => server.name);
    console.log("Servers: " + _.join(serverNames, ", "));
});

bot.on('presence', presenceEvent);