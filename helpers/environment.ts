export class Configuration {
    private static getVar(key: string) {
        return process.env.key;
    }

    public static get DISCORD_TOKEN() {
        return this.getVar("DISCORD_TOKEN");
    }

    public static get DB_HOST() {
        return this.getVar("DB_HOST");
    }

    public static get DB_USERNAME() {
        return this.getVar("DB_USERNAME");
    }

    public static get DB_PASSWORD() {
        return this.getVar("DB_PASSWORD");
    }

    public static get DB_SCHEMA() {
        return this.getVar("LURKER_SCHEMA");
    }
}