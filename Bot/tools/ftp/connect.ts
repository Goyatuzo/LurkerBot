import * as Client from 'ftp';

interface FtpConnection {
    connect: () => void;
    connection: Client;
}

export default FtpConnection;