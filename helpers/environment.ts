export class Configuration {
    private static getVar(key: string) {
        return process.env[key];
    }

    public static get DISCORD_TOKEN() {
        return this.getVar("DISCORD_TOKEN");
    }

    public static get DB_HOST() {
        return this.getVar("LURKER_DB");
    }

    public static get DB_USERNAME() {
        return this.getVar("DB_USERNAME");
    }

    public static get DB_PASSWORD() {
        return this.getVar("DB_PW");
    }

    public static get DB_SCHEMA() {
        return this.getVar("LURKER_SCHEMA");
    }
}