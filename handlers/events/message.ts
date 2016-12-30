import {Message, TextChannel} from "discord.js";
import * as _ from "lodash";

import statsMessageHandler from "../messages/stats";

export default function (message: Message) {
    const client = message.client;
    let msg = message.cleanContent;

    if (_.startsWith(msg, "stats")) {
        console.log(`Stats summary requested on ${(message.channel as TextChannel).guild.name}`);
        statsMessageHandler(message);
    }
}