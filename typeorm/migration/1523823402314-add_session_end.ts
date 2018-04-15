import {MigrationInterface, QueryRunner} from "typeorm";

export class add_session_end1523823402314 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` ADD `sessionEndDate` datetime NOT NULL");
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` DROP `sessionEndDate`");
    }

}
