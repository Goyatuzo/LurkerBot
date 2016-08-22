import connection from './connection';
import ITime from '../classes/i-times';

// Cache of the summary of the database.
var summaryCache: Array<ITime>;

// Cache of user sums.
var cache: { [key: string]: ITime } = {};

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
    // First check to see if the cache already contains the values.
    if (summaryCache) {
        console.log(`Retrieving DB summary from cache.`);
        callback(summaryCache);
        return;
    }

    // If it does not, then query the DB.
    const stmt: string = `SELECT U.name, T.gameName, SUM(T.duration) AS duration
                            FROM Times T
                            JOIN Users U ON T.id = U.id GROUP BY T.gameName, U.name ORDER BY T.gameName`;

    const start: Date = new Date();

    // First store the results in the cache and then execute any subsequent methods.
    _runQueryNoParams(stmt, results => {
        summaryCache = results;
        callback(results);
    });
}

/**
 * Get the summary of times from a particular server specified by serverId.
 * @param {string} serverId The ID of the server.
 * @param callback
 */
export function getTimesFromServer(serverId: string, callback) {
    const serverKey = `serverSum${serverId}`;

    if (cache[serverKey]) {
        console.log(`Retrieving SERVER summary from cache.`);
        callback(cache[serverKey]);
        return;
    }

    const stmt: string = `SELECT U.name, D.gameName, D.duration FROM (SELECT T.id, T.gameName, SUM(T.duration) AS duration
                            FROM Times T
                            WHERE T.id IN (SELECT userId from ServersToUsers WHERE serverId = ${serverId}) GROUP BY T.gameName, T.id ORDER BY T.gameName) as D
                            JOIN Users U ON D.id = U.id ORDER BY D.gameName`;

    // First store the results in the cache and then execute any subsequent methods.
    _runQueryNoParams(stmt, results => {
        cache[serverKey] = results;
        callback(results);
    });
}

/**
 * Get the list of times a particular user has played a game.
 * @param {string} userId The user ID to query the DB for.
 * @param callback
 */
export function getTimesFromUser(userId: string, callback) {
    const userKey = `userSum${userId}`;

    if (cache[userKey]) {
        console.log(`Retrieving USER summary from cache.`);
        callback(cache[userKey]);
        return;
    }

    const stmt: string = `SELECT U.name, gameName, SUM(duration) AS duration
                            FROM Times T JOIN Users U
                            ON T.id = U.id
                            WHERE U.id = ${userId}
                            GROUP BY gameName ORDER BY duration DESC`;

    // First store the results in the cache and then execute any subsequent methods.
    _runQueryNoParams(stmt, results => {
        cache[userKey] = results;
        callback(results);
    });
}

/**
 * Get the list of servers with the ID and the name.
 * @param callback
 */
export function getServerList(callback) {
    const stmt: string = `SELECT id, name FROM Servers`;

    _runQueryNoParams(stmt, callback);
}

/**
 * Get the complete list of users associated with the input serverId.
 * @param {string} serverId The id of the server to retrieve the list of users from.
 * @param callback
 */
export function getAllUsers(serverId: string, callback) {
    const stmt: string = `SELECT id, name FROM Users`;

    _runQueryNoParams(stmt, callback);
}