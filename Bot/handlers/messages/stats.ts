import {Message, Client} from "discord.js";

export default function (message: string, client: Client) {
    const tokens = message.split(" ");

    // If the user only requests stats, then print out the server's stats.
    if (tokens.length === 0) {

    }
}