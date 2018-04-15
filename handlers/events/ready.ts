import * as _ from 'lodash';

import { Client } from "discord.js";

import { getRepository } from 'typeorm';
import { DiscordDBServer } from '../../typeorm/models/discord-db-server';
import { DiscordDBUser } from '../../typeorm/models/discord-db-user';

// 30 minutes
const deleteInterval = 1000 * 60 * 30;

export default async function (bot: Client) {
    const serverRepository = getRepository(DiscordDBServer);
    const userRepository = getRepository(DiscordDBUser);

    // We want the name of the servers, but while we're at it, populate the table.
    const guildNames = await Promise.all(bot.guilds.array().map(async guild => {
        let serverMatch = await serverRepository.findOneById(guild.id, { relations: ['users'] });

        if (!serverMatch) {
            serverMatch = serverRepository.create();
            serverMatch.id = guild.id;
        }

        serverMatch.name = guild.name;
        serverRepository.save(serverMatch);

        const users = await Promise.all(guild.members.array().map(async guildMember => {
            let userMatch = await userRepository.findOneById(guildMember.id);

            if (!userMatch) {
                userMatch = userRepository.create();
                userMatch.id = guildMember.id;
            }

            userMatch.username = `${guildMember.displayName}`;

            if (!userMatch.servers) {
                userMatch.servers = [serverMatch];
            }
            else if (!serverMatch.users.some(dbUser => dbUser.id === userMatch.id)) {
                console.log("NO MATCH");
                userMatch.servers.push(serverMatch);
            }

            return userMatch;
        }));

        userRepository.save(users);

        return guild.name;
    }));

    console.log(`Connected to: ${guildNames.join(",")}`);
}