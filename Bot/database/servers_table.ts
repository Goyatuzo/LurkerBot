import {Server} from "discord.js";
import * as mysql from "mysql";

import connection from "../database/connection";
import * as UserMethods from "../tools/user_methods";
import * as _ from "lodash";

// The Times table to store the times.
connection.query(`
    CREATE TABLE IF NOT EXISTS Servers (
        id          VARCHAR(25) NOT NULL    PRIMARY KEY,
        name        VARCHAR(25) NOT NULL,
        region      VARCHAR(20) NOT NULL,
        icon        VARCHAR(150)
    )`
);

export function updateServer(server: Server) {
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