package com.lurkerbot.core.discordUser

interface DiscordUserRepository {
    fun getAllUsers(): Map<String, UserInDiscord>

    fun getUserInDiscord(userId: String): UserInDiscord?

    fun saveUserInDiscord(user: UserInDiscord)

    fun removeUserInDiscord(user: UserInDiscord)
}
