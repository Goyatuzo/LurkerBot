import { Entity, Column, ManyToOne, ObjectIdColumn, ObjectID } from "typeorm";

@Entity()
export class GameTime {
    @ObjectIdColumn()
    _id: ObjectID;

    @Column({ type: "char", length: 19 })
    userId: string;

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
}
