import {MigrationInterface, QueryRunner} from "typeorm";

export class initial1523594661676 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("CREATE TABLE `game_time` (`id` varchar(255) NOT NULL PRIMARY KEY, `secondsPlayed` int(11) NOT NULL, `discordUserId` int(11)) ENGINE=InnoDB");
        await queryRunner.query("CREATE TABLE `discord_d_b_user` (`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, `username` varchar(255) NOT NULL, `discriminator` char(4) NOT NULL) ENGINE=InnoDB");
        await queryRunner.query("ALTER TABLE `game_time` ADD CONSTRAINT `fk_8fb82afc6193a996712201d1146` FOREIGN KEY (`discordUserId`) REFERENCES `discord_d_b_user`(`id`)");
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `game_time` DROP FOREIGN KEY `fk_8fb82afc6193a996712201d1146`");
        await queryRunner.query("DROP TABLE `discord_d_b_user`");
        await queryRunner.query("DROP TABLE `game_time`");
    }

}
