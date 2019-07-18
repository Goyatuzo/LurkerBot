import { Message, Client, TextChannel, VoiceChannel, Collection, GuildMember } from "discord.js";

export default function (message: Message, tokens: string[]) {
    const num = parseInt(tokens[2]);
    const channelArg = tokens[3];

    if (isNaN(num)) {
        console.error(`Pickone function called with invalid number ${num}`);
        return;
    }

    const channel = message.guild.channels.find(channel => channel.name === channelArg) as (TextChannel | VoiceChannel);

    if (!channel) {
        console.error(`Invalid channel ${channelArg} was queried on guild ${message.guild.name}`);
        return;
    }

    message.channel.send(`Picking ${num} users in ${channelArg}...`);

    message.channel.send(pickN(channel.members, num));
}

function pickN(users: Collection<string, GuildMember>, n: number): string {
    const guildMembers = users.array().filter(member => !member.user.bot && member.user.presence.status === "online");

    if (guildMembers.length === 0)
        return `There are no users in the specified channel.`;
    else if (n > guildMembers.length)
        return guildMembers.map(member => ` - ${member.displayName}#${member.user.discriminator}`).join("\n");
    else {
        // Taken from https://jsperf.com/k-random-elements-from-array/2
        let result: GuildMember[] = new Array(n);
        let taken = new Array(n);
        let len = guildMembers.length;

        while (n--) {
            const randNum = Math.floor(Math.random() * len);
            console.log(`RAND: ${randNum}`);
            const toAdd = guildMembers[randNum];
            result[n] = guildMembers[randNum in taken ? taken[randNum] : randNum];
            taken[randNum] = --len;
        }

        return result.map(member => ` - ${member.displayName}#${member.user.discriminator}`).join("\n");
    }
}