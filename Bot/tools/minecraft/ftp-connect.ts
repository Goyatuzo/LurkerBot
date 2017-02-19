import FtpConnection from '../ftp/connect';
import * as Client from 'ftp';

const options: Client.Options = {
    host: process.env["MC_FTP_HOST"],
    port: process.env["MC_FTP_PORT"],
    user: process.env["MC_FTP_USERNAME"],
    password: process.env["MC_FTP_PASSWORD"]
}

class MinecraftFtp implements FtpConnection {
    private ftp: Client;

    constructor() {
        this.ftp = new Client();
    }

    connect(): void {
        this.connection.connect(options);
    }

    get connection(): Client {
        return this.ftp;
    }
}

let McFtp = new MinecraftFtp();
export default McFtp;