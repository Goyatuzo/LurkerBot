import { Entity, PrimaryGeneratedColumn, Column, Generated, OneToMany, OneToOne } from "typeorm";
import { GameTime } from "./game-time";

@Entity()
export class DiscordDBUser {
    @PrimaryGeneratedColumn()
    id: string;

    @Column("varchar")
    username: string;

    @Column({ type: "char", length: 4 })
    discriminator: string;

    @OneToMany(type => GameTime, gameTime => gameTime.discordUser)
    gameTimes: GameTime[];
}
