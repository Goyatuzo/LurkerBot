import { Entity, Column, ManyToOne, ObjectIdColumn, ObjectID } from "typeorm";
import { DiscordDBUser } from "./discord-db-user";

@Entity()
export class GameTime {
    @ObjectIdColumn()
    id: ObjectID;

    @Column("int")
    secondsPlayed: number;

    @Column("datetime")
    sessionEndDate: Date;

    @Column("varchar")
    gameName: string;

    @Column({ type: "varchar", nullable: true })
    gameType: string;

    @Column({ type: "varchar", nullable: true })
    gameState: string;

    @Column({ type: "varchar", nullable: true })
    gameDetail: string;

    @ManyToOne(type => DiscordDBUser, discordUser => discordUser.gameTimes)
    discordUser: DiscordDBUser;
}
