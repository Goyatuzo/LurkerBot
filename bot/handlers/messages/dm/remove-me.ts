import { DiscordDBUser } from "../../../../typeorm/models/discord-db-user";
import { Message } from "discord.js";
import { getRepository, FindManyOptions } from "typeorm";
import { GameTime } from "../../../../typeorm/models/game-time";

export default async function renmoveMeHandler(message: Message) {
    try {
        const discordUser = message.author;
        const userRepository = getRepository(DiscordDBUser);
        const gameTimeRepo = getRepository(GameTime);

        const gameTimes = await gameTimeRepo.find({ where: { discordUser: { id: discordUser.id } } });

        gameTimeRepo.delete(gameTimes.map(times => times.id));

        const users = await userRepository.findByIds([discordUser.id]);
        users.forEach(user => userRepository.delete(user));

        console.log(`Removed ${discordUser.username}#${discordUser.discriminator}`);
        message.reply(`${discordUser.username}#${discordUser.discriminator} has been removed. All your game stats are gone.`);
    } catch (err) {
        console.error(err);
    }
}