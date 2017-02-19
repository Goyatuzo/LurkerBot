import {Message, TextChannel} from "discord.js";
import * as _ from "lodash";

import messageRouter from "../messages";

export default function (message: Message): void {
    const client = message.client;
    const msgTokens = message.cleanContent.split(' ');

    if (msgTokens.length === 0) {
        return;
    }

    if (msgTokens[0].toLowerCase() === "!lurkerbot") {
        messageRouter(message);
    }
}