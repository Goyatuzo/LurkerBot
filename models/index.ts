import "reflect-metadata";

import { getConnectionManager } from 'typeorm';

getConnectionManager().createAndConnect({
    driver: {
        type: "mysql",
        host: process.env.LURKER_DB,
        port: 3306,
        username: process.env.LURKER_USERNAME,
        password: process.env.LURKER_PASSWORD,
        database: process.env.LURKER_SCHEMA
    },
    entities: [
        __dirname + "/models/*.js"
    ],
    autoSchemaSync: true
}).then(conn => {
    console.log("Sync successful.");
}).catch(err => {
    console.log(err);
});

export { getConnectionManager };
export * from './models/user';
export * from './models/server';