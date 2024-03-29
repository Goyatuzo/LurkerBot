package com.lurkerbot.discordUser

import com.lurkerbot.core.discordUser.DiscordUserRepository
import com.lurkerbot.core.discordUser.UserInDiscord
import dev.kord.core.entity.User

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

    fun addUser(user: User) {
        discordUserRepository.saveUserInDiscord(user.toUserInDiscord())
        notBeingTracked.remove(user.id.toString())
        beingTracked[user.id.toString()] = user.toUserInDiscord()
    }

    fun removeUser(user: User) {
        discordUserRepository.removeUserInDiscord(user.toUserInDiscord())
        notBeingTracked.add(user.id.toString())
        beingTracked.remove(user.id.toString())
    }
}
