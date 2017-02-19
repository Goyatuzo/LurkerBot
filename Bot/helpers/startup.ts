import McFtp from '../tools/ftp/connect';

export default class Startup {
    private static checkEnv(name: string): void {
        if (!process.env[name]) {
            console.error(`Missing ${name}`);
        }
    }

    public static runInitialCheck(): void {
        this.checkEnv("DISCORD_TOKEN");
        this.checkEnv("LURKER_DB");
        this.checkEnv("LURKER_USERNAME");
        this.checkEnv("LURKER_PASSWORD");
        this.checkEnv("MC_FTP_USERNAME");
        this.checkEnv("MC_FTP_PASSWORD");
        this.checkEnv("MC_FTP_HOST");
        this.checkEnv("MC_FTP_PORT");
    }

    public static init(): void {
        McFtp.connect();
    }
}
