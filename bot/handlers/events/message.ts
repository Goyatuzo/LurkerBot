import { Message, TextChannel, Client, DMChannel } from "discord.js";
import * as _ from "lodash";

import messageRouter from "../messages/text";

export default function (bot: Client) {
    return function (message: Message): void {
        if (message.channel instanceof TextChannel) {
            // If the message dosen't begin with an at mention at lurkerbot, just ignore it.
            if (!message.cleanContent.startsWith(`!${bot.user.username.toLocaleLowerCase()}`))
                return;

            messageRouter(message);
        } else if (message.channel instanceof DMChannel) {
            console.log("DM");
        }
    }
} 