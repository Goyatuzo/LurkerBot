package com.lurkerbot.discordUser

import me.jakejmattson.discordkt.api.dsl.commands
import org.litote.kmongo.KMongo

@Suppress("unused")
fun userCommands() =
    commands("DiscordUser") {
        val mongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))
        val userRepository = DiscordUserRepository(mongoClient)
        val userTracker = UserTracker(userRepository)

        slash("addme") {
            description = "Start tracking your gametime."
            execute { userTracker.addUser(author) }
        }
    }
