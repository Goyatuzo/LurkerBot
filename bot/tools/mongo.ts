import { MongoClient, Db } from 'mongodb';
import { Configuration } from '../../helpers/environment'

let connectedDb: Db;

export function connect(callback) {
    MongoClient.connect(Configuration.DB_HOST).then(cl => {
        connectedDb = cl.db('lurker-bot');

        callback(cl);
    }).catch(err => {
        console.error(err);
    });
}

export { connectedDb };
