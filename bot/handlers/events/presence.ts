import { GuildMember } from "discord.js";

import * as mkdirp from "mkdirp";
import * as path from "path";

import gameTracker from "../../tools/stats/game_tracker";

import * as UserMethods from "../../tools/user_methods";

export default function (before: GuildMember, after: GuildMember) {
    // If either is a bot, then do not process it at all.
    if (before.user.bot|| after.user.bot) {
        return;
    }

    // If the two users are identical, process it further.
    if (before.id === after.id) {
        gameTracker(before, after);
    }
}