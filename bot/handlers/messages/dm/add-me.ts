import { DiscordDBUser } from "../../../../typeorm/models/discord-db-user";
import { Message } from "discord.js";
import { getRepository } from "typeorm";

export default function addMeHandler(message: Message) {
    const discordUser = message.author;
    const userRepository = getRepository(DiscordDBUser);

    const newUser = userRepository.create();
    newUser.id = discordUser.id;
    newUser.discriminator = discordUser.discriminator;
    newUser.username = discordUser.username;

    userRepository.save(newUser);

    console.log(`Added ${discordUser.username}#${discordUser.discriminator}`);
    message.reply(`${discordUser.username}#${discordUser.discriminator} has been added to the user list. Your game time stats are now being recorded.`);
}