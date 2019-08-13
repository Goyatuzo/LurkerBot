import { DiscordDBUser } from "../models/discord-db-user";
import { getMongoRepository, MongoRepository } from "typeorm";

export class DiscordDBUserHelper {
    private static _users: { [id: string]: DiscordDBUser };

    public static async getUser(id: string): Promise<DiscordDBUser> {
        const userRepository = getMongoRepository(DiscordDBUser);

        if (!(id in this._users)) {
            const match = await userRepository.findOne({ userId: id });

            if (match) {
                this._users[id] = match;
            } else {
                this._users[id] = null;
            }
        }

        return this._users[id];
    }

    public static addUser(user: DiscordDBUser) {
        const userRepository = getMongoRepository(DiscordDBUser);

        userRepository.save(user);

        this._users[user.userId] = user;
    }

    public static removeUser(user: DiscordDBUser) {
        this._users[user.userId] = null;
    }
}