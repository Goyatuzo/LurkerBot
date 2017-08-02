import {Guild} from "discord.js";
import * as mysql from "mysql";

import connection from "../database/connection";
import * as UserMethods from "../tools/user_methods";

// The Servers table to store the server information.
connection.query(`
    CREATE TABLE IF NOT EXISTS Servers (
        id          VARCHAR(25) NOT NULL    PRIMARY KEY,
        name        VARCHAR(25) NOT NULL,
        region      VARCHAR(20) NOT NULL,
        icon        VARCHAR(150)
    ) CHARACTER SET utf8 COLLATE utf8_unicode_ci`
);

// The Servers to Users table to store the mappings between Servers and their users.
connection.query(`
    CREATE TABLE IF NOT EXISTS ServersToUsers (
        id          VARCHAR(40) NOT NULL  PRIMARY KEY,
        serverId    VARCHAR(25) NOT NULL,
        userId      VARCHAR(25) NOT NULL
    ) CHARACTER SET utf8 COLLATE utf8_unicode_ci`
);


/**
 * Update or push in a new server object to the SQL database.
 * @param server
 */
export function updateServer(server: Guild) {
    const stmt = `INSERT INTO Servers (id, name, region, icon) VALUES (?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                        name=?, region=?, icon=?`;
    const prepared = mysql.format(stmt, [server.id, server.name, server.region, server.iconURL, server.name, server.region, server.iconURL]);

    connection.query(prepared, err => {
        if (err) {
            console.log(err);
        }
    });
}

/**
 * Update the mapping for the users list on a server.
 * @param server
 */
export function updateServerUserMap(server: Guild) {
    const userIds = _.map(server.members.array(), user => user.id);
    const serverId = server.id;

    const stmt = `INSERT INTO ServersToUsers (id, serverId, userId) VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                        serverId=?, userId=?`;

    userIds.map(userId => {
        let prepared = mysql.format(stmt, [serverId + userId, serverId, userId, serverId, userId]);

        connection.query(prepared, err => {
            if (err) {
                console.log(err);
            }
        });
    });
}