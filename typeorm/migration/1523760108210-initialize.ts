import {MigrationInterface, QueryRunner} from "typeorm";

export class initialize1523760108210 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("CREATE TABLE `game_time` (`id` varchar(255) NOT NULL PRIMARY KEY, `secondsPlayed` int(11) NOT NULL, `discordUserId` char(18)) ENGINE=InnoDB");
        await queryRunner.query("CREATE TABLE `discord_d_b_user` (`id` char(18) NOT NULL, `username` varchar(255) NOT NULL, `discriminator` char(4) NOT NULL, PRIMARY KEY(`id`)) ENGINE=InnoDB");
        await queryRunner.query("CREATE TABLE `discord_d_b_server_users_discord_d_b_user` (`discordDBServerId` char(18) NOT NULL, `discordDBUserId` char(18) NOT NULL, PRIMARY KEY(`discordDBServerId`, `discordDBUserId`)) ENGINE=InnoDB");
        await queryRunner.query("ALTER TABLE `lurker-test`.`discord_d_b_server` CHANGE `id` `id` char(18) NOT NULL");
        await queryRunner.query("ALTER TABLE `game_time` ADD CONSTRAINT `fk_8fb82afc6193a996712201d1146` FOREIGN KEY (`discordUserId`) REFERENCES `discord_d_b_user`(`id`)");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` ADD CONSTRAINT `fk_6caa8ffc1fd9161d73fba4b5c6c` FOREIGN KEY (`discordDBServerId`) REFERENCES `discord_d_b_server`(`id`)");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` ADD CONSTRAINT `fk_94d1e5a3251d105fddd937e8252` FOREIGN KEY (`discordDBUserId`) REFERENCES `discord_d_b_user`(`id`)");
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` DROP FOREIGN KEY `fk_94d1e5a3251d105fddd937e8252`");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` DROP FOREIGN KEY `fk_6caa8ffc1fd9161d73fba4b5c6c`");
        await queryRunner.query("ALTER TABLE `game_time` DROP FOREIGN KEY `fk_8fb82afc6193a996712201d1146`");
        await queryRunner.query("ALTER TABLE `lurker-test`.`discord_d_b_server` CHANGE `id` `id` char(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL");
        await queryRunner.query("DROP TABLE `discord_d_b_server_users_discord_d_b_user`");
        await queryRunner.query("DROP TABLE `discord_d_b_user`");
        await queryRunner.query("DROP TABLE `game_time`");
    }

}
