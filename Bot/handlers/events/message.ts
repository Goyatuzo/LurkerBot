import {Message} from "discord.js";
import * as _ from "lodash";

import statsMessageHandler from "../messages/stats";

export default function (message: Message) {
    const client = message.client;
    let msg = message.cleanContent;

    if (_.startsWith(msg, "stats")) {
        msg = msg.substring(("stats").length);
        statsMessageHandler(msg, client);
    }
}