import {MigrationInterface, QueryRunner} from "typeorm";

export class add_gamename1523825544987 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` ADD `gameName` varchar(255) NOT NULL");
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` DROP `gameName`");
    }

}
