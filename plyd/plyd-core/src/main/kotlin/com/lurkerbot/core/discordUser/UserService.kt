package com.lurkerbot.core.discordUser

class UserService(private val discordUserRepository: DiscordUserRepository) {
    fun getUserByDiscordId(userId: String): UserInDiscord? =
        discordUserRepository.getUserInDiscord(userId)
}
