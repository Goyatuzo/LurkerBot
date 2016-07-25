import * as Discord from "discord.js";
import * as _ from "lodash";

import presenceEvent from "./handlers/events/presence";

import {updateServer} from "./database/servers_table";

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
    // We want the name of the servers, but while we're at it, populate the table.
    const serverNames = _.map(bot.servers, server => {
        // Iterate through the list of servers, and add each one to the table.
        updateServer(server);
        
        return server.name;
    });
    console.log("Servers: " + _.join(serverNames, ", "));


});

bot.on('presence', presenceEvent);