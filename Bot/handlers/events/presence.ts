import {User} from "discord.js";

import * as mkdirp from "mkdirp";
import * as path from "path";

import * as UserMethods from "../../tools/user_methods";
import {isBot} from "../../tools/bots";


export default function (before: User, after: User) {
    // If either is a bot, then do not process it at all.
    if (isBot(before) || isBot(after)) {
        console.log(`${UserMethods.getUniqueUsername(before)} is a bot.`);
        return;
    }

    // If the two users are identical, process it further.
    if (before.id === after.id) {
        console.log("Same guy");
    } else {
        console.log(`${UserMethods.getUniqueUsername(before)} and ${UserMethods.getUniqueUsername(after)}`);
    }
}