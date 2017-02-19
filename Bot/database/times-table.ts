import { GuildMember } from "discord.js";
import * as mysql from "mysql";

import connection from "../database/connection";
import * as UserMethods from "../tools/user_methods";
import * as _ from "lodash";
import * as fs from 'fs';
import { EOL } from 'os';

const tableName = 'Times';

// The Times table to store the times.
connection.query(`
    CREATE TABLE IF NOT EXISTS ${tableName} (
        id          VARCHAR(25) NOT NULL,
        endTime     DATETIME    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
        gameName    VARCHAR(45) NOT NULL,
        duration    INT(6)      NOT NULL
    ) CHARACTER SET utf8 COLLATE utf8_unicode_ci`
);

/**
 * Write a new entry into the Times table.
 * @param user
 * @param duration
 */
export function writeNewTimeRow(user: GuildMember, duration: number) {
    const game = UserMethods.getGameName(user);

    const stmt = `INSERT INTO ${tableName} (id, gameName, duration) VALUES (${user.id}, ?, ${duration})`;
    const prepared = mysql.format(stmt, [game]);

    const begin = +(new Date());

    if (duration > 0 || game.match(/[^\x00-\x7F]*/g).length > 0) {
        connection.query(prepared, (err, results) => {
            if (err) {
                console.log(err);
                return;
            }

            console.log(`TIME INSERT query took: ${(+(new Date()) - begin) / 1000} seconds`);
            console.log(`Saving stats for ${UserMethods.getUniqueUsername(user)} playing ${game} for ${duration}`);
        });
    } else {
        console.log(`Error adding the time. ${game} was played for ${duration} seconds`);
    }
}

/**
 * Get the sum of the duration of all the users passed into the function.
 * @param users
 */
export function getDurationSum(users: Array<GuildMember>, callback: (results: any) => any) {
    const userIds = users.map(user => user.id);
    const idString = _.join(userIds, ", ");

    const stmt = `SELECT gameName AS name, SUM(duration) AS duration FROM ${tableName} WHERE id in (${idString}) GROUP BY gameName`;

    const begin = +(new Date());

    connection.query(stmt, (err, results) => {
        if (err) {
            console.log(err);
            return;
        }

        console.log(`TOTAL SUM query took: ${(+(new Date()) - begin) / 1000} seconds`);
        callback(results);
    });
}

/**
 * Get the sum of the duration of all the users passed into the function, then sort by the time.
 * @param users
 * @param callback
 */
export function getDurationSumTimeSorted(users: Array<GuildMember>, callback: (results: any) => any) {
    const userIds = users.map(user => user.id);
    const idString = _.join(userIds, ", ");

    const stmt = `SELECT gameName AS name, SUM(duration) AS duration FROM ${tableName} WHERE id in (${idString}) GROUP BY gameName ORDER BY duration DESC`;

    const begin = +(new Date());

    connection.query(stmt, (err, results) => {
        if (err) {
            console.log(err);
            return;
        }

        console.log(`TOTAL SUM SORTED BY TIME query took: ${(+(new Date()) - begin) / 1000} seconds`);
        callback(results);
    });
}

function generatePreparedTemplateToClearGames(listOfGames: Array<string>): string {
    let questionMarks = new Array(listOfGames.length + 1).join("?,");
    questionMarks = questionMarks.slice(0, -1);

    return `(${questionMarks})`;
}

/**
 * Clear all database entries that have any of the games listed in the array.
 * @param listOfGames
 */
export function clearDatabase() {
    fs.readFile('./resources/games-ignored.txt', 'utf8', (err, data) => {
        if (err) {
            return (console.log(err));
        }

        const games = data.split(EOL).map(game => game.trim());

        const stmt = `DELETE FROM ${tableName} WHERE gameName IN ${generatePreparedTemplateToClearGames(games)}`;
        const prepared = mysql.format(stmt, games);
        const begin = +(new Date());

        connection.query(prepared, (err, data) => {
            if (err) {
                console.log(err);
                return;
            }

            console.log(`DELETE IGNORED GAMES took: ${(+(new Date()) - begin) / 1000} seconds`);
        });
    });
}