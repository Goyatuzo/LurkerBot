import { DiscordDBUser } from "../../../../typeorm/models/discord-db-user";
import { Message } from "discord.js";
import { getMongoRepository } from "typeorm";

export default async function renmoveMeHandler(message: Message) {
    try {
        const discordUser = message.author;
        const userRepository = getMongoRepository(DiscordDBUser);

        const users = await userRepository.find({ userId: discordUser.id });
        users.forEach(user => userRepository.delete(user));

        console.log(`Removed ${discordUser.username}#${discordUser.discriminator}`);
        message.reply(`${discordUser.username}#${discordUser.discriminator} has been removed. All your game stats are gone.`);
    } catch (err) {
        console.error(err);
    }
}