package com.lurkerbot.discordUser

import dev.kord.core.entity.User

data class UserInDiscord(val discriminator: String, val userId: String, val username: String)

fun User.toUserInDiscord(): UserInDiscord =
    UserInDiscord(
        discriminator = this.discriminator,
        userId = this.id.asString,
        username = this.username
    )
