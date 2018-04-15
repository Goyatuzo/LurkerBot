import { GuildMember } from "discord.js";
import * as UserMethods from "../user_methods";

import stats from "../stats/stats";
import { getRepository } from "typeorm";
import { GameTime } from "../../../typeorm/models/game-time";
import { DiscordDBUser } from "../../../typeorm/models/discord-db-user";
import { GameType } from "../../../helpers/discord-js-enums";

/**
 * When the user starts playing a game, call this function.
 * @param user
 */
function beginLogging(user: GuildMember, game: string) {
    console.log(`${UserMethods.getUniqueUsername(user)} is now playing ${game}`);

    console.log(user.presence.game.type);
    if (!user.presence.game.streaming && user.presence.game.type === GameType.PLAYING) {
        stats.addGame(user, game);
    }
}

/**
 * When the user stops playing a game, call this function.
 * @param user
 */
async function endLogging(user: GuildMember, game: string) {
    console.log(`${UserMethods.getUniqueUsername(user)} stopped playing ${game}`);

    const seconds: number = stats.timePlayed(user, game);
    stats.removeGame(user, game);

    if (seconds === null) {
        console.log("Invalid seconds, skipping this log.");
        return;
    }

    const gameTimeRepository = getRepository(GameTime);
    const userRepository = getRepository(DiscordDBUser);
    const match = await userRepository.findOneById(user.id);

    let newEntry = await gameTimeRepository.create();
    newEntry.secondsPlayed = seconds;
    newEntry.sessionEndDate = new Date(Date.now());
    newEntry.discordUser = match;
    newEntry.gameName = game;

    gameTimeRepository.save(newEntry);

    // If a valid number of seconds, be sure to add it to the database.
    // writeNewTimeRow(user, seconds);
}

export default function (before: GuildMember, after: GuildMember) {
    const beforeGame = before.user.presence.game;
    const afterGame = after.user.presence.game;
    // If the user has a game on before, that means they quit that game, unless it's the same for both.
    if (beforeGame) {
        console.log(`${beforeGame.name} type ${beforeGame.type} is now ${afterGame.name} type ${afterGame.type}`)
    }
    if (beforeGame && !(beforeGame.equals(afterGame))) {
        endLogging(before, beforeGame.name);
    }

    // If the user has a game on after, that means they began playing the game.
    if (afterGame) {
        beginLogging(after, afterGame.name);
    }
}