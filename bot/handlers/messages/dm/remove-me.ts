import { DiscordDBUser } from "../../../../typeorm/models/discord-db-user";
import { Message } from "discord.js";
import { getMongoRepository } from "typeorm";
import { DiscordDBUserHelper } from "../../../../typeorm/helpers/discord-db-user-helper";
import { GameTime } from "../../../../typeorm/models/game-time";

export default async function renmoveMeHandler(message: Message) {
    try {
        const discordUser = message.author;
        const userRepository = getMongoRepository(DiscordDBUser);
        const timeRepository = getMongoRepository(GameTime);

        const gameTimes = await timeRepository.find({ userId: discordUser.id });

        // Doing it the right way caused some unintended casualties.
        gameTimes.forEach(time => timeRepository.delete(time));

        const users = await userRepository.find({ userId: discordUser.id });
        users.forEach(user => DiscordDBUserHelper.removeUser(user));

        console.log(`Removed ${discordUser.username}#${discordUser.discriminator}`);
        message.reply(`${discordUser.username}#${discordUser.discriminator} has been removed. All your game stats are gone.`);
    } catch (err) {
        message.reply("Something went wrong. Your stats have not yet been deleted yet.")
        console.error(err);
    }
}