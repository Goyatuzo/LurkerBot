import Timer from "../tools/timer";
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

