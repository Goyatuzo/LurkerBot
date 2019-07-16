import {MigrationInterface, QueryRunner} from "typeorm";

export class TableNameUpdate1563257247540 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` DROP FOREIGN KEY `fk_6caa8ffc1fd9161d73fba4b5c6c`");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` DROP FOREIGN KEY `fk_94d1e5a3251d105fddd937e8252`");
        await queryRunner.query("CREATE INDEX `IDX_5934c587d81885f68cddb0b9a4` ON `discord_d_b_server_users_discord_d_b_user` (`discordDBServerId`)");
        await queryRunner.query("CREATE INDEX `IDX_7e09a78d2ae478e13707c6f901` ON `discord_d_b_server_users_discord_d_b_user` (`discordDBUserId`)");
        await queryRunner.query("ALTER TABLE `game_time` ADD CONSTRAINT `FK_daad436fdcd799c64f80f4a28bd` FOREIGN KEY (`discordUserId`) REFERENCES `discord_d_b_user`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` ADD CONSTRAINT `FK_5934c587d81885f68cddb0b9a4f` FOREIGN KEY (`discordDBServerId`) REFERENCES `discord_d_b_server`(`id`) ON DELETE CASCADE ON UPDATE NO ACTION");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` ADD CONSTRAINT `FK_7e09a78d2ae478e13707c6f9017` FOREIGN KEY (`discordDBUserId`) REFERENCES `discord_d_b_user`(`id`) ON DELETE CASCADE ON UPDATE NO ACTION");
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` DROP FOREIGN KEY `FK_7e09a78d2ae478e13707c6f9017`");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` DROP FOREIGN KEY `FK_5934c587d81885f68cddb0b9a4f`");
        await queryRunner.query("ALTER TABLE `game_time` DROP FOREIGN KEY `FK_daad436fdcd799c64f80f4a28bd`");
        await queryRunner.query("DROP INDEX `IDX_7e09a78d2ae478e13707c6f901` ON `discord_d_b_server_users_discord_d_b_user`");
        await queryRunner.query("DROP INDEX `IDX_5934c587d81885f68cddb0b9a4` ON `discord_d_b_server_users_discord_d_b_user`");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` ADD CONSTRAINT `fk_94d1e5a3251d105fddd937e8252` FOREIGN KEY (`discordDBUserId`, `discordDBUserId`) REFERENCES `discord_d_b_user`(`id`,`id`) ON DELETE RESTRICT ON UPDATE RESTRICT");
        await queryRunner.query("ALTER TABLE `discord_d_b_server_users_discord_d_b_user` ADD CONSTRAINT `fk_6caa8ffc1fd9161d73fba4b5c6c` FOREIGN KEY (`discordDBServerId`, `discordDBServerId`) REFERENCES `discord_d_b_server`(`id`,`id`) ON DELETE RESTRICT ON UPDATE RESTRICT");
    }

}
