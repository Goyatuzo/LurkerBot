import connection from './connection';
import ITime from '../classes/i-times';

/**
 * Get the list of the summary of times by users.
 * @param callback
 */
export function getTimesByUser(callback) {
    const stmt: string = `SELECT U.name, T.gameName, SUM(T.duration) AS duration FROM Immutable.Times T JOIN Users U ON T.id = U.id GROUP BY T.gameName, U.name ORDER BY T.gameName`;

    const start: Date = new Date();
    connection.query(stmt, (err, results: Array<ITime>) => {
        if (err) {
            console.log(err);
            return;
        }

        callback(results);
    });
}

export function getTimesFromServer(serverId: string, callback) {
    const stmt: string = `SELECT U.name, D.gameName, D.duration FROM (SELECT T.id, T.gameName, SUM(T.duration) AS duration
                            FROM Immutable.Times T
                            WHERE T.id IN (SELECT userId from ServersToUsers WHERE serverId = ${serverId}) GROUP BY T.gameName, T.id ORDER BY T.gameName) as D
                            JOIN Users U ON D.id = U.id ORDER BY D.gameName`;
    
    connection.query(stmt, (err, results: Array<ITime>) => {
        if (err) {
            console.log(err);
            return;
        }

        callback(results);
    });
}