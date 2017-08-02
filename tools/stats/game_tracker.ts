import { GuildMember} from "discord.js";
import * as UserMethods from "../user_methods";

import stats from "../stats/stats";

import {writeNewTimeRow} from "../../database/times-table";

/**
 * When the user starts playing a game, call this function.
 * @param user
 */
function beginLogging(user: GuildMember, game: string) {
    console.log(`${UserMethods.getUniqueUsername(user)} is now playing ${game}`);

    if (!user.presence.game.streaming) {
        stats.addGame(user, game);
    }
}

/**
 * When the user stops playing a game, call this function.
 * @param user
 */
function endLogging(user: GuildMember, game: string) {
    console.log(`${UserMethods.getUniqueUsername(user)} stopped playing ${game}`);

    const seconds: number = stats.timePlayed(user, game);
    stats.removeGame(user, game);

    if (seconds === null) {
        console.log("Invalid seconds, skipping this log.");
        return;
    }

    // If a valid number of seconds, be sure to add it to the database.
    writeNewTimeRow(user, seconds);
}

export default function (before: GuildMember, after: GuildMember) {
    let game: string;

    // If the user has a game on before, that means they quit that game.
    game = UserMethods.getGameName(before);
    if (game) {
        endLogging(before, game);
    }

    // If the user has a game on after, that means they began playing the game.
    game = UserMethods.getGameName(after);
    if (game) {
        beginLogging(after, game);
    }
}