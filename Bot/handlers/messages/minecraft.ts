import { Message } from "discord.js";
import * as ReadLine from 'readline';

import McFtp from '../../tools/ftp/connect';
import mcData from '../../tools/minecraft/storage';

export default function (message: Message): void {
    const client = message.client;
    const msgTokens = message.cleanContent.split(' ');

    if (!msgTokens[2]) {

    } else if (msgTokens[2].toLowerCase() === "deaths") {
        processMinecraftData(message, deathSummary);
    } else if (msgTokens[2].toLowerCase() === "death-details") {
        processMinecraftData(message, deathDetails);
    }
}

function deathDetails(): string {
    let deathString = "";

    mcData.deathsArray.forEach(death => {
        deathString += `${death.playerName}\n`;

        death.causes.forEach(deathCause => {
            deathString += `\t- ${deathCause}\n`;
        });
    });

    mcData.clearCache();

    return deathString;
}

function deathSummary(): string {
    let deathString = "";

    mcData.deathsArray.forEach(death => {
        deathString += `${death.playerName} has died ${death.causes.length} times\n`;
    });

    mcData.clearCache();

    return deathString;
}

function processMinecraftData(message: Message, processData: () => string): void {
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
            message.channel.sendMessage(processData()).catch(error => {
                console.error(error);
            });
        });
    });
}