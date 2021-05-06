import { Message } from "discord.js";
import { connectedDb } from '../../../tools/mongo';

export default async function renmoveMeHandler(message: Message) {
    try {
        const discordUser = message.author;
        const userCollection = connectedDb.collection('discord_db_user');
        const timeCollection = connectedDb.collection('game_time')

        timeCollection.deleteMany({ userId: discordUser.id });
        userCollection.deleteOne({ userId: discordUser.id });
        // Doing it the right way caused some unintended casualties.

        console.log(`Removed ${discordUser.username}#${discordUser.discriminator}`);
        message.reply(`${discordUser.username}#${discordUser.discriminator} has been removed. All your game stats are gone.`);
    } catch (err) {
        message.reply("Something went wrong. Your stats have not yet been deleted yet.")
        console.error(err);
    }
}