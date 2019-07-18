import { Message, TextChannel, Client } from "discord.js";
import * as _ from "lodash";

import messageRouter from "../messages";

export default function (bot: Client) {
    return function(message: Message): void {
        console.log(message);

        // If the message dosen't begin with an at mention at lurkerbot, just ignore it.
        if (!message.cleanContent.startsWith(`!${bot.user.username.toLocaleLowerCase()}`))
            return;

        messageRouter(message);
    }
} 