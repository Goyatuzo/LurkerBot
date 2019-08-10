import { ConnectionOptions } from 'typeorm';
import * as fs from 'fs';
import { Configuration } from '../helpers/environment';

console.log(Configuration.DB_HOST)

export const connectionOptions: ConnectionOptions = {
    type: "mongodb",
    url: Configuration.DB_HOST,
    synchronize: true,
    logging: false,
    useNewUrlParser: true,
    entities: [
        "typeorm/models/**/*.ts"
    ],
    migrations: [
        "typeorm/migration/**/*.ts"
    ],
    subscribers: [
        "typeorm/subscriber/**/*.ts"
    ]
};