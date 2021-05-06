import { Message } from "discord.js";

import { connectedDb } from '../../../tools/mongo';

export default function addMeHandler(message: Message) {
    const discordUser = message.author;

    const collection = connectedDb.collection('discord_db_user');

    collection.findOne({ userId: discordUser.id }).then(match => {
        console.log(`${discordUser.username}#${discordUser.discriminator} already being tracked`);
        message.reply(`You're already being tracked. No action was taken at this time.`);
    }).catch(err => {
        const newUser = {
            userId: discordUser.id,
            discriminator: discordUser.discriminator,
            username: discordUser.username
        }

        collection.insertOne(newUser);

        console.log(`Added ${discordUser.username}#${discordUser.discriminator}`);
        message.reply(`${discordUser.username}#${discordUser.discriminator} has been added to the user list. Your game time stats are now being recorded.`);
    });
}