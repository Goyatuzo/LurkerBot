import { Message, DMChannel } from "discord.js";
import addMeHandler from "./add-me";
import renmoveMeHandler from "./remove-me";
export default function (message: Message) {
    const client = message.client;
    const msgTokens = message.cleanContent.split(' ');

    // If the author of the message is a bot, then ignore.
    if (message.author.bot) {
        return;
    }

    if (message.cleanContent.toLowerCase() === "addme") {
        addMeHandler(message);
    } else if (message.cleanContent.toLowerCase() === "removeme") {
        renmoveMeHandler(message);
    } else {
        message.reply(`Could not find an appropriate command that matches your query. At the moment, only 'addme' and 'removeme' are functional.`)
    }
}