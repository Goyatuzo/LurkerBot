import { Message } from "discord.js";
import * as ReadLine from 'readline';

import McFtp from '../../tools/ftp/connect';
import mcData from '../../tools/minecraft/storage';

export default function (message: Message): void {
    McFtp.connection.get('server.log', (error, fileStream) => {
        if (error) {
            console.error(error);
            return;
        }

        let readLine = ReadLine.createInterface({
            input: fileStream
        });

        readLine.on('line', (line: string) => {
            mcData.processLine(line);
        });

        readLine.on('close', () => {
            let deathString = "";
            
            mcData.deathsArray.forEach(death => {
                deathString += `${death.playerName} has died ${death.causes.length} times\n`;
            });

            message.channel.sendMessage(deathString).catch(error => {
                console.error(error);
            });
        });
    });
}
