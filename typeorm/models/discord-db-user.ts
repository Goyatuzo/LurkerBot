import { Entity, PrimaryColumn, Column, Generated, OneToMany, OneToOne, ManyToMany } from "typeorm";
import { GameTime } from "./game-time";
import { DiscordDBServer } from "./discord-db-server";

@Entity({ name: "discord_d_b_user" })
export class DiscordDBUser {
    @PrimaryColumn({ type: "char", length: 19 })
    id: string;

    @Column("varchar")
    username: string;

    @Column({ type: "char", length: 4 })
    discriminator: string;

    @OneToMany(type => GameTime, gameTime => gameTime.discordUser)
    gameTimes: GameTime[];

    @ManyToMany(type => DiscordDBServer, server => server.users)
    servers: DiscordDBServer[];
}
