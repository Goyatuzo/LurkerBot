import connection from './connection';
import ITime from '../classes/i-times';

/**
 * Get the list of the summary of times by users.
 * @param callback
 */
export function getTimesByUser(callback) {
    const stmt: string = `SELECT U.name, T.gameName, SUM(T.duration) AS duration FROM Immutable.Times T JOIN lurkerbot.Users U ON T.id = U.id GROUP BY T.gameName, U.name ORDER BY T.gameName`;

    const start: Date = new Date();
    connection.query(stmt, (err, results: Array<ITime>) => {
        if (err) {
            console.log(err);
            return;
        }

        callback(results);
    });
}