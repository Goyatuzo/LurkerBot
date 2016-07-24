import * as Discord from "discord.js";
import presenceEvent from "./handlers/events/presence";
import * as _ from "lodash";

/**
 * BOT token = process.env.DISCORD_TOKEN
 * DB URL = process.env.GAMEDB
 * DB Username = process.env.USERNAME
 * DB Password = process.env.PASSWORD
 * DB Default = process.env.DBNAME
 */

var bot = new Discord.Client();
bot.loginWithToken(process.env.DISCORD_TOKEN);

bot.on('ready', event => {
    _.map(bot.servers, server => {
        console.log(server.id);
    });
});

bot.on('presence', presenceEvent);