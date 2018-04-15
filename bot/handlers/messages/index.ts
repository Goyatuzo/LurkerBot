import { Message, TextChannel } from "discord.js";

import statsHandler from './stats';
import minecraftHandler from './minecraft';

export default function routeCommand(message: Message) {
    const client = message.client;
    const msgTokens = message.cleanContent.split(' ');

    if (msgTokens.length <= 1) {
        console.log(`No command specified. Displaying help.`);
        return;
    }

    if (msgTokens[1].toLowerCase() === "stats") {
        console.log(`Stats summary requested on ${(message.channel as TextChannel).guild.name}`);
        statsHandler(message);
    } else if (msgTokens[1].toLowerCase() === "minecraft") {
        console.log(`Minecraft requested on ${(message.channel as TextChannel).guild.name}`);
        minecraftHandler(message);
    }
}