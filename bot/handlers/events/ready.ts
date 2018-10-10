import * as _ from 'lodash';

import { Client } from "discord.js";

import { getRepository } from 'typeorm';
import { DiscordDBServer } from '../../../typeorm/models/discord-db-server';
import { DiscordDBUser } from '../../../typeorm/models/discord-db-user';

export default async function (bot: Client) {
    const serverRepository = getRepository(DiscordDBServer);
    const userRepository = getRepository(DiscordDBUser);

    const storedServers = await serverRepository.find({ relations: ['users'] });
    const storedUsers = await userRepository.find();

    let localServers: DiscordDBServer[] = [];
    let localUsers: DiscordDBUser[] = [];

    // We want the name of the servers, but while we're at it, populate the table.
    const guildNames = bot.guilds.array().map(guild => {
        let serverMatch = storedServers.find(dbServer => guild.id === dbServer.id);

        if (!serverMatch) {
            serverMatch = serverRepository.create({
                id: guild.id
            });
        }

        serverMatch.name = guild.name;

        localServers.push(serverMatch);

        const users = guild.members.array().map(guildMember => {
            const queryId = guildMember.user.id;

            let userMatch = storedUsers.find(dbUser => dbUser.id === queryId) || localUsers.find(userArr => userArr.id === queryId);

            if (!userMatch) {
                userMatch = userRepository.create({
                    id: queryId
                });
            }

            userMatch.username = guildMember.user.username;
            userMatch.discriminator = guildMember.user.discriminator

            if (!userMatch.servers) {
                userMatch.servers = [serverMatch];
            }
            else if (!serverMatch.users.some(dbUser => dbUser.id === userMatch.id)) {
                userMatch.servers.push(serverMatch);
            }

            return userMatch;
        });


        console.log(`Saved ${guild.name}`);

        return guild.name;
    });

    await serverRepository.save(localServers);
    await userRepository.save(localUsers);

    console.log(`Connected to all servers.`);
}