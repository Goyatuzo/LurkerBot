import { Entity, Column, ManyToOne, ObjectIdColumn, ObjectID, Index } from "typeorm";

@Entity()
export class GameTime {
    @ObjectIdColumn()
    _id: ObjectID;

    @Column({ type: "char", length: 19 })
    @Index()
    userId: string;

    @Column("datetime")
    sessionBegin: Date;

    @Column("datetime")
    sessionEnd: Date;

    @Column("varchar")
    @Index()
    gameName: string;

    @Column({ type: "varchar", nullable: true })
    gameType: string;

    @Column({ type: "varchar", nullable: true })
    gameState: string;

    @Column({ type: "varchar", nullable: true })
    gameDetail: string;

    @Column({ type: "varchar", nullable: true })
    otherDetails: string;
}
