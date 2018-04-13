import * as _ from 'lodash';

import { Client } from "discord.js";

// 30 minutes
const deleteInterval = 1000 * 60 * 30;

export default function (bot: Client) {
    // We want the name of the servers, but while we're at it, populate the table.
    const serverNames = _.map(bot.guilds.array(), guild => {
        // Iterate through the list of servers, and add each one to the database.
        // updateServer(guild);

        // // Update the server to user mappings.
        // updateServerUserMap(guild);

        // const users = guild.members.array().map(member => member.user);
        // // Add all the users to the database.
        // _.map(users, user => updateUser(user));

        return guild.name;
    });
    console.log("Servers: " + _.join(serverNames, ", "));
}