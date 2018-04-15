import {MigrationInterface, QueryRunner} from "typeorm";

export class initalize1523777877753 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("CREATE TABLE `game_time` (`id` varchar(255) NOT NULL PRIMARY KEY, `secondsPlayed` int(11) NOT NULL, `discordUserId` char(19)) ENGINE=InnoDB");
        await queryRunner.query("CREATE TABLE `discord_d_b_server_users_discord_d_b_user` (`discordDBServerId` char(19) NOT NULL, `discordDBUserId` char(19) NOT NULL, PRIMARY KEY(`discordDBServerId`, `discordDBUserId`)) ENGINE=InnoDB");
        await queryRunner.query("ALTER TABLE `discord_d_b_user` CHANGE `id` `id` char(19) NOT NULL");
        await queryRunner.query("ALTER TABLE `discord_d_b_server` CHANGE `id` `id` char(19) NOT NULL");
        await queryRunner.query("ALTER TABLE `game_time` ADD CONSTRAINT `fk_8fb82afc6193a996712201d1146` FOREIGN KEY (`discordUserId`) REFERENCES `discord_d_b_user`(`id`)");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` ADD CONSTRAINT `fk_6caa8ffc1fd9161d73fba4b5c6c` FOREIGN KEY (`discordDBServerId`) REFERENCES `discord_d_b_server`(`id`)");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` ADD CONSTRAINT `fk_94d1e5a3251d105fddd937e8252` FOREIGN KEY (`discordDBUserId`) REFERENCES `discord_d_b_user`(`id`)");
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` DROP FOREIGN KEY `fk_94d1e5a3251d105fddd937e8252`");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` DROP FOREIGN KEY `fk_6caa8ffc1fd9161d73fba4b5c6c`");
        await queryRunner.query("ALTER TABLE `game_time` DROP FOREIGN KEY `fk_8fb82afc6193a996712201d1146`");
        await queryRunner.query("ALTER TABLE `discord_d_b_server` CHANGE `id` `id` char(18) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL");
        await queryRunner.query("ALTER TABLE `discord_d_b_user` CHANGE `id` `id` char(18) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL");
        await queryRunner.query("DROP TABLE `discord_d_b_server_users_discord_d_b_user`");
        await queryRunner.query("DROP TABLE `game_time`");
    }

}
