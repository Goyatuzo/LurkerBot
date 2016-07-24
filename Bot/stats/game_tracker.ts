import {User} from "discord.js";
import * as UserMethods from "../tools/user_methods";

import stats from "../stats/stats";

import connection from "../database/connection";

// The Times table to store the times.
connection.execute(`
    CREATE TABLE IF NOT EXISTS Times (
        id          VARCHAR(25) NOT NULL,
        endTime     DATETIME    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
        gameName    VARCHAR(45) NOT NULL,
        duration    INT(6)      NOT NULL
    )`
);

/**
 * When the user starts playing a game, call this function.
 * @param user
 */
function beginLogging(user: User) {

}

/**
 * When the user stops playing a game, call this function.
 * @param user
 */
function endLogging(user: User) {

}

export default function (before: User, after: User) {
    let game: string;

    // If the user has a game on before, that means they quit that game.
    game = UserMethods.getGameName(before);
    if (game) {
        endLogging(before);
    }

    // If the user has a game on after, that means they began playing the game.
    game = UserMethods.getGameName(after);
    if (game) {
        beginLogging(after);
    }
}