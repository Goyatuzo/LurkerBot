import { Client } from 'discord.js';

import { getConnectionManager } from '../../models';

export default function readyEvent(client: Client) {
    return () => {
        const guilds = client.guilds.array();
        console.log(`Connected to: ${guilds.map(guild => guild.name).join(',')}`);
    }
}