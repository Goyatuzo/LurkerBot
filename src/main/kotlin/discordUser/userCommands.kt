package com.lurkerbot.discordUser

import com.lurkerbot.internal.MongoDatabase
import me.jakejmattson.discordkt.api.dsl.commands

@Suppress("unused")
fun userCommands() =
    commands("DiscordUser") {
        val mongoClient = MongoDatabase.client
        val userRepository = DiscordUserRepository(mongoClient)
        val userTracker = UserTracker(userRepository)

        slash("add-me") {
            description = "Start tracking your game time."
            execute { userTracker.addUser(author) }
        }

        slash("remove-me") {
            description = "Stop tracking your game time."
            execute { userTracker.removeUser(author) }
        }
    }
