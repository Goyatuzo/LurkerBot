package com.lurkerbot.discordUser

import me.jakejmattson.discordkt.api.dsl.commands
import org.litote.kmongo.KMongo

@Suppress("unused")
fun userCommands() =
    commands("DiscordUser") {
        val mongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))
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
