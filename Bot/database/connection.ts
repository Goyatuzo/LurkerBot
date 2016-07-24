import * as mysql from "mysql";
import * as fs from "fs";

const connectionDetails: mysql.IConnectionConfig = {
    host: process.env.LURKER_DB,
    port: 3306,
    user: process.env.LURKER_USERNAME,
    password: process.env.LURKER_PASSWORD,
    database: process.env.LURKER_SCHEMA
};

// Hold the database connection on this variable.
var connection: mysql.IConnection;

/**
 * Connect to the database and create the Times table.
 */
function _connect() {
    // If there is already a connection, make sure to end it.
    if (typeof (connection) !== "undefined") {
        connection.end();
    }

    connection = mysql.createConnection(connectionDetails);

    connection.connect(err => {
        if (err) {
            console.log(err);
            return;
        };
    });

    _disconnectHandler();
};

/**
 * Create a handler for when the SQL server connection is broken, or any kind of error happens.
 */
function _disconnectHandler() {
    connection.on('error', err => {
        console.log("SQL server error.");
        console.log(err);

        _connect();
    });
}

// Connect to the database here.
_connect();

export default connection;