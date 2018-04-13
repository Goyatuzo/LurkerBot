import { ConnectionOptions } from 'typeorm';
import * as fs from 'fs';
import { Configuration } from '../helpers/environment';

export const connectionOptions: ConnectionOptions = {
    type: "mysql",
    host:  Configuration.DB_HOST,
    username: Configuration.DB_USERNAME,
    password: Configuration.DB_PASSWORD,
    database: Configuration.DB_SCHEMA,
    synchronize: false,
    logging: false,
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