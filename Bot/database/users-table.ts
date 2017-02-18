import {User} from "discord.js";
import * as mysql from "mysql";

import connection from "../database/connection";
import * as UserMethods from "../tools/user_methods";
import * as _ from "lodash";

// The Times table to store the times.
connection.query(`
    CREATE TABLE IF NOT EXISTS Users (
        id          VARCHAR(25) NOT NULL    PRIMARY KEY,
        name        VARCHAR(40) NOT NULL,
        avatar      VARCHAR(150)
    ) CHARACTER SET utf8 COLLATE utf8_unicode_ci`
);

/**
 * Update or push in a new server object to the SQL database.
 * @param server
 */
export function updateUser(user: User) {
    const uniqueName = UserMethods.getUniqueUsername(user);

    const stmt = `INSERT INTO Users (id, name, avatar) VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                        name=?, avatar=?`;
    const prepared = mysql.format(stmt, [user.id, uniqueName, user.avatarURL, uniqueName, user.avatarURL]);

    connection.query(prepared, err => {
        if (err) {
            console.log(err);
        }
    });
}