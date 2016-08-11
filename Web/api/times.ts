import connection from './connection';
import ITime from '../classes/i-times';

/**
 * Since code that runs the query seems to be duplicated in each function that's exported in this file, it is a
 * helper function now to help ease file size.
 * @param {string} query The query to be executed.
 * @param callback
 */
function _runQueryNoParams(query: string, callback) {
    connection.query(query, (err, results: Array<ITime>) => {
        if (err) {
            console.log(err);
            return;
        }

        callback(results);
    });
}

/**
 * Get the list of the summary of times by users.
 * @param callback
 */
export function getTimesByUser(callback) {
    const stmt: string = `SELECT U.name, T.gameName, SUM(T.duration) AS duration
                            FROM Immutable.Times T
                            JOIN Users U ON T.id = U.id GROUP BY T.gameName, U.name ORDER BY T.gameName`;

    const start: Date = new Date();
    _runQueryNoParams(stmt, callback);
}

/**
 * Get the summary of times from a particular server specified by serverId.
 * @param {string} serverId The ID of the server.
 * @param callback
 */
export function getTimesFromServer(serverId: string, callback) {
    const stmt: string = `SELECT U.name, D.gameName, D.duration FROM (SELECT T.id, T.gameName, SUM(T.duration) AS duration
                            FROM Immutable.Times T
                            WHERE T.id IN (SELECT userId from ServersToUsers WHERE serverId = ${serverId}) GROUP BY T.gameName, T.id ORDER BY T.gameName) as D
                            JOIN Users U ON D.id = U.id ORDER BY D.gameName`;

    _runQueryNoParams(stmt, callback);
}

/**
 * Get the list of times a particular user has played a game.
 * @param {string} userId The user ID to query the DB for.
 * @param callback
 */
export function getTimesFromUser(userId: string, callback) {
    const stmt: string = `SELECT U.name, gameName, SUM(duration) AS duration
                            FROM Immutable.Times T JOIN Users U
                            ON T.id = U.id
                            WHERE U.id = ${userId}
                            GROUP BY gameName ORDER BY duration DESC`;

    _runQueryNoParams(stmt, callback);
}