package com.lurkerbot.discordUser

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
}