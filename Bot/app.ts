import * as Discord from "discord.js";
import * as _ from "lodash";

import presenceEvent from "./handlers/events/presence";

import {updateServer, updateServerUserMap} from "./database/servers_table";
import {updateUser} from "./database/users_table";

import messageEvent from "./handlers/events/message";

/**
 * BOT token = process.env.DISCORD_TOKEN
 * DB URL = process.env.LURKER_DB
 * DB Username = process.env.LURKER_USERNAME
 * DB Password = process.env.LURKER_PASSWORD
 * DB Default = process.env.LURKER_SCHEMA
 */

var bot = new Discord.Client();
bot.login(process.env.DISCORD_TOKEN);

bot.on('ready', event => {

    // We want the name of the servers, but while we're at it, populate the table.
    const serverNames = _.map(bot.guilds.array(), guild => {
        // Iterate through the list of servers, and add each one to the database.
        updateServer(guild);

        // Update the server to user mappings.
        updateServerUserMap(guild);

        const users = guild.members.array().map(member => member.user);
        // Add all the users to the database.
        _.map(users, user => updateUser(user));
        
        return guild.name;
    });
    console.log("Servers: " + _.join(serverNames, ", "));
});

bot.on('presenceUpdate', presenceEvent);
bot.on('message', messageEvent);
