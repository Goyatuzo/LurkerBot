import * as _ from 'lodash';

import { Client } from "discord.js";

import { getRepository } from 'typeorm';
import { DiscordDBServer } from '../../typeorm/models/discord-db-server';
import { DiscordDBUser } from '../../typeorm/models/discord-db-user';

// 30 minutes
const deleteInterval = 1000 * 60 * 30;

export default function (bot: Client) {
    // We want the name of the servers, but while we're at it, populate the table.
    const serverNames = bot.guilds.array().map(async guild => {
        const serverRepository = getRepository(DiscordDBServer);
        const userRepository = getRepository(DiscordDBUser);

        let match = await serverRepository.findOneById(guild.id);

        if (!match) {
            match = serverRepository.create();
            match.id = guild.id;
        }

        match.name = guild.name;

        await serverRepository.save(match);

        // // Update the server to user mappings.
        // updateServerUserMap(guild);

        // const users = guild.members.array().map(member => member.user);
        // // Add all the users to the database.
        // _.map(users, user => updateUser(user));

        return guild.name;
    });
    console.log("Servers: " + serverNames.join(","));
}