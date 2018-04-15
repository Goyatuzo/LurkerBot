import { Message } from "discord.js";
import * as ReadLine from 'readline';

import McFtp from '../../tools/minecraft/ftp-connect';
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

function deathDetails(): Array<string> {
    let deathStrings: Array<string> = [];

    mcData.deathsArray.forEach(death => {
        let playerString = `${death.playerName}\n`;

        death.entries.forEach(logLine => playerString += `\t- ${logLine.text} on ${logLine.time.format("MMM Do hh:mm:ss A")} \n`);
        deathStrings.push(playerString);
    });

    mcData.clearCache();
    return deathStrings;
}

function deathSummary(): Array<string> {
    let deathString = "";

    mcData.deathsArray.forEach(death => {
        deathString += `${death.playerName} has died ${death.entries.length} times\n`;
    });

    mcData.clearCache();

    return [deathString];
}

function processMinecraftData(message: Message, processData: () => Array<string>): void {
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
            processData().forEach(data => {
                message.channel.sendMessage(data).catch(error => {
                    console.error(error);
                });
            });


        });
    });
}