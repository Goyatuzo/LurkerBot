import { GuildMember } from "discord.js";
import * as UserMethods from "../user_methods";

import stats from "../stats/stats";
import { getMongoRepository } from "typeorm";
import { GameTime } from "../../../typeorm/models/game-time";
import { DiscordDBUser } from "../../../typeorm/models/discord-db-user";
import { GameType } from "../../../helpers/discord-js-enums";

/**
 * When the user starts playing a game, call this function.
 * @param user
 */
function beginLogging(user: GuildMember, game: string) {
    if (!user.presence.game.streaming && user.presence.game.type === GameType.PLAYING) {
        stats.addGame(user, game);
    }
}

/**
 * When the user stops playing a game, call this function.
 * @param user
 */
async function endLogging(user: GuildMember, game: string) {
    const seconds: number = stats.timePlayed(user, game);
    stats.removeGame(user, game);

    if (seconds === null || seconds === 0) {
        console.error("Invalid seconds, skipping this log.");
        return;
    }

    const userRepository = getMongoRepository(DiscordDBUser);
    const match = await userRepository.findOne({ userId: user.id });

    if (!match) {
        console.error(`No user with id ${user.id} and username ${user.displayName} was found when adding gametime.`);
        return;
    }

    let newEntry = new GameTime();
    newEntry.secondsPlayed = seconds;
    newEntry.sessionEndDate = new Date(Date.now());
    newEntry.gameName = game;
    newEntry.userId = match.userId;

    if (game === "League of Legends") {
        newEntry.gameState = user.presence.game.state;
        newEntry.gameDetail = user.presence.game.assets ? user.presence.game.assets.largeText : null;
        newEntry.gameType = JSON.stringify({
            map: user.presence.game.details ? user.presence.game.details : null
        });
    }

    const gameTimeRepository = getMongoRepository(GameTime);

    gameTimeRepository.insertOne(newEntry);

    console.log(`Logged ${newEntry.secondsPlayed} seconds for ${match.username} playing ${newEntry.gameName}`);
    // If a valid number of seconds, be sure to add it to the database.
    // writeNewTimeRow(user, seconds);
}

export default function (before: GuildMember, after: GuildMember) {
    const beforeGameName = UserMethods.getGameName(before);
    const afterGameName = UserMethods.getGameName(after);

    const userQuitGame = beforeGameName && beforeGameName !== afterGameName;
    const presenceChanged = beforeGameName === afterGameName && JSON.stringify(before.presence.game) !== JSON.stringify(after.presence.game);

    console.log(userQuitGame || presenceChanged);

    // If the user has a game on before, that means they quit that game.
    if (userQuitGame || presenceChanged) {
        endLogging(before, beforeGameName);
    }

    // If the user has a game on after, that means they began playing the game
    if (afterGameName) {
        beginLogging(after, afterGameName);
    }
}