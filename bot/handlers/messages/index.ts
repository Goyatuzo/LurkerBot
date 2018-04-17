import { Message, TextChannel } from "discord.js";

import statsHandler from './stats';
import minecraftHandler from './minecraft';
import pickoneHandler from './pickone';

export default function routeCommand(message: Message) {
    const client = message.client;
    const msgTokens = message.cleanContent.split(' ');

    if (msgTokens.length <= 1) {
        console.log(`No command specified. Displaying help.`);
        return;
    }

    if (msgTokens[1].toLowerCase() === "stats") {
        console.log(`Stats summary requested on ${message.guild.name}`);
        statsHandler(message);
    } else if (msgTokens[1].toLowerCase() === "minecraft") {
        console.log(`Minecraft requested on ${message.guild.name}`);
        minecraftHandler(message);
    } else if (msgTokens[1].toLowerCase() === "pick") {
        console.log(`Picking one user in ${message.guild.name}`);
        pickoneHandler(message, msgTokens);
    }
}