import {MigrationInterface, QueryRunner} from "typeorm";

export class nullableColumns1563481227684 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` CHANGE `gameType` `gameType` varchar(255) NULL");
        await queryRunner.query("ALTER TABLE `game_time` CHANGE `gameState` `gameState` varchar(255) NULL");
        await queryRunner.query("ALTER TABLE `game_time` CHANGE `gameDetail` `gameDetail` varchar(255) NULL");
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` CHANGE `gameDetail` `gameDetail` varchar(255) NOT NULL");
        await queryRunner.query("ALTER TABLE `game_time` CHANGE `gameState` `gameState` varchar(255) NOT NULL");
        await queryRunner.query("ALTER TABLE `game_time` CHANGE `gameType` `gameType` varchar(255) NOT NULL");
    }

}
