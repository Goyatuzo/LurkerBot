import {MigrationInterface, QueryRunner} from "typeorm";

export class addGameDetails1563260278276 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` ADD `gameType` varchar(255) NOT NULL");
        await queryRunner.query("ALTER TABLE `game_time` ADD `gameState` varchar(255) NOT NULL");
        await queryRunner.query("ALTER TABLE `game_time` ADD `gameDetail` varchar(255) NOT NULL");
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` DROP COLUMN `gameDetail`");
        await queryRunner.query("ALTER TABLE `game_time` DROP COLUMN `gameState`");
        await queryRunner.query("ALTER TABLE `game_time` DROP COLUMN `gameType`");
    }

}
