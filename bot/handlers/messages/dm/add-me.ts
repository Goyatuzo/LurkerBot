import { DiscordDBUser } from "../../../../typeorm/models/discord-db-user";
import { Message, User } from "discord.js";
import {  getMongoRepository } from "typeorm";

export default function addMeHandler(message: Message) {
    const discordUser = message.author;
    const userRepository = getMongoRepository(DiscordDBUser);

    const newUser = new DiscordDBUser();
    newUser.userId = discordUser.id;
    newUser.discriminator = discordUser.discriminator;
    newUser.username = discordUser.username;

    userRepository.save(newUser);

    console.log(`Added ${discordUser.username}#${discordUser.discriminator}`);
    message.reply(`${discordUser.username}#${discordUser.discriminator} has been added to the user list. Your game time stats are now being recorded.`);
}