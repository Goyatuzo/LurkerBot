package com.lurkerbot.discordUser

import com.lurkerbot.core.discordUser.UserInDiscord
import dev.kord.core.entity.User

fun User.toUserInDiscord(): UserInDiscord =
    UserInDiscord(
        discriminator = this.discriminator,
        userId = this.id.toString(),
        username = this.username
    )
