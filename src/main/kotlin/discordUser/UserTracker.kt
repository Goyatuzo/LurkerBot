package com.lurkerbot.discordUser

class UserTracker(private val discordUserRepository: DiscordUserRepository) {
    private val beingTracked: MutableMap<String, UserInDiscord> = mutableMapOf()
    private val notBeingTracked: MutableSet<String> = mutableSetOf()

    fun initialize() {
        beingTracked.putAll(discordUserRepository.getAllUsers())
    }

    fun userIsBeingTracked(userId: String): Boolean {
        if (beingTracked.containsKey(userId)) return true
        if (notBeingTracked.contains(userId)) return false

        discordUserRepository.getUserInDiscord(userId)?.let {
            beingTracked[userId] = it
            return true
        }

        notBeingTracked.add(userId)
        return false
    }
}
