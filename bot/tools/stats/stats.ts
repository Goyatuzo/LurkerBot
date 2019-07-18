import Timer from "./game_timer";
import { GuildMember } from "discord.js";
import * as UserMethods from "../user_methods";

export interface GameList {
    [gameName: string]: Timer;
}

export class Stats {
    stats: { [userId: string]: GameList } = {};

    private _addUser(user: GuildMember) {
        const userId = user.id;

        // If the id is not already in stats, create it and give it empty dictionary.
        if (!(userId in this.stats)) {
            this.stats[userId] = {};
        }
    }

    /**
     * Add a game to be tracked.
     * @param user
     * @param game
     */
    addGame(user: GuildMember, game: string) {
        const userId = user.id;

        this._addUser(user);

        let games: GameList = this.stats[userId];

        // Only create a new timer if one doesn't already exist.
        if (!(game in games)) {
            games[game] = new Timer();
        }
    }

    /**
     * Query the time played for that user playing the specified game.
     * @param user
     * @param game
     */
    timePlayed(user: GuildMember, game: string) {
        const userId = user.id;

        // If the user is in the stats array, then check to see if the game is as well.
        if (userId in this.stats) {
            const games = this.stats[userId];

            // Check to see if the game is in the game array.
            if (game in games) {
                return games[game].timeElapsed();
            }
        }

        return null;
    }

    /**
     * Check to see if a user is already playing a game.
     * @param user
     * @param game
     */
    exists(user: GuildMember, game: string) {
        // If the ID is already being tracked.
        if (user.id in this.stats) {
            const gameList: GameList = this.stats[user.id];

            // AND the game is being tracked as well, then return true.
            if (game in gameList) {
                return true;
            }
        }

        return false;
    }

    /**
     * When a user quits playing the game, remove it from the stats data structure.
     * @param user
     * @param game
     */
    removeGame(user: GuildMember, game: string) {
        if (this.exists(user, game)) {
            delete this.stats[user.id][game];
        }
    }
}

// Export a new instance of stats.
var stats = new Stats();
export default stats;