import {GuildMember, User} from "discord.js";
import * as _ from "lodash";

/**
 * Return a string of the username with its discriminator added
 * like how the username is dispalyed on Discord.
 * @param user
 */
export function getUniqueUsername(user: GuildMember | User) {
    if (user instanceof GuildMember) {
        user = user.user;
    }

    return user.username + '#' + user.discriminator.toString();
}

/**
 * Get the name of the game the user is playing. Undefined if none exists.
 * @param user
 */
export function getGameName(user: GuildMember) {
    const game = user.presence.game;

    if (game) {
        return game;
    }

    return undefined;
}