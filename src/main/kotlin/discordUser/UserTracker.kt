package com.lurkerbot.discordUser

import dev.kord.core.entity.User

class UserTracker(private val discordUserRepository: DiscordUserRepository) {
    private val beingTracked: MutableMap<String, UserInDiscord> = mutableMapOf()

    fun userIsBeingTracked(userId: String): Boolean {
        if (beingTracked.containsKey(userId)) return true

        discordUserRepository.getUserInDiscord(userId)?.let {
            beingTracked[userId] = it
            return true
        }

        return false
    }

    fun addUser(user: User) {
        discordUserRepository.saveUserInDiscord(user.toUserInDiscord())
    }

    fun removeUser(user: User) {
        discordUserRepository.removeUserInDiscord(user.toUserInDiscord())
    }
}
