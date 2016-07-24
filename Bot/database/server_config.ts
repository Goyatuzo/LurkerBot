export default {
    host: process.env.LURKER_DB,
    port: 3306,
    user: process.env.LURKER_USERNAME,
    password: process.env.LURKER_PASSWORD,
    database: process.env.LURKER_SCHEMA
};