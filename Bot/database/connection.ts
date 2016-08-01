import * as mysql from "mysql";
import * as fs from "fs";

const connectionDetails: mysql.IConnectionConfig = {
    host: process.env.LURKER_DB,
    port: 3306,
    user: process.env.LURKER_USERNAME,
    password: process.env.LURKER_PASSWORD,
    database: process.env.LURKER_SCHEMA,
    multipleStatements: true
};

// Hold the database connection on this variable.
var connection: mysql.IConnection;

/**
 * Create a handler for when the SQL server connection is broken, or any kind of error happens.
 */
function _disconnectHandler() {
    connection = mysql.createConnection(connectionDetails);

    connection.connect(err => {
        if (err) {
            console.log("Error on trying to connect to DB.");
            console.log(err);
            _disconnectHandler();
        }
    });

    connection.on('error', err => {
        console.log("SQL server error.");
        console.log(err);
        _disconnectHandler();
    });
}

// Connect to the database here.
_disconnectHandler();

export default connection;