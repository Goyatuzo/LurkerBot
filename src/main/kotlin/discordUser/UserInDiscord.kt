package com.lurkerbot.discordUser


data class UserInDiscord(
    val discriminator: String,
    val userId: String,
    val username: String
)