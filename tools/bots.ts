import * as path from "path";
import * as fs from "fs";
import {User} from "discord.js";

// Platform specific EOL signifiers.
import {EOL} from "os";

/**
 * The list of bot ids.
 */
const botIds = fs.readFileSync(path.join(__dirname, "../resources/bots.txt"), "utf-8").split(EOL);

export function isBot(user: User) {
    return user.bot || botIds.some(id => id === user.id);
}