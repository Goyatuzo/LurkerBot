import { Message, DMChannel } from "discord.js";
import addMeHandler from "./add-me";
import renmoveMeHandler from "./remove-me";
export default function (message: Message) {
    const client = message.client;
    const msgTokens = message.cleanContent.split(' ');

    if (message.cleanContent.toLowerCase() === "addme") {
        addMeHandler(message);
    } else if (message.cleanContent.toLowerCase() === "removeme") {
        renmoveMeHandler(message);
    }
}