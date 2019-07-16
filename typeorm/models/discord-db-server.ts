import { Entity, PrimaryGeneratedColumn, Column, Generated, ManyToMany, JoinTable, PrimaryColumn } from "typeorm";
import { GameTime } from "./game-time";
import { DiscordDBUser } from "./discord-db-user";

@Entity({ name: "discord_d_b_server" })
export class DiscordDBServer {
    @PrimaryColumn({ type: "char", length: 19 })
    id: string;

    @Column("varchar")
    name: string;

    @ManyToMany(type => DiscordDBUser, user => user.servers)
    @JoinTable()
    users: DiscordDBUser[];
}
