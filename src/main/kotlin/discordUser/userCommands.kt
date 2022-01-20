package com.lurkerbot.discordUser

import com.lurkerbot.internal.Dependencies
import me.jakejmattson.discordkt.commands.commands

@Suppress("unused")
fun userCommands() =
    commands("DiscordUser") {
        val userTracker = Dependencies.userTracker

        globalSlash("add-me") {
            description = "Start tracking your game time, or update your username."
            execute {
                userTracker.addUser(author)

                respond(
                    "You are now tracked as **${author.username}#${author.discriminator}**",
                    ephemeral = true
                )
            }
        }

        globalSlash("remove-me") {
            description =
                "Stop tracking your future game sessions. Will not delete existing records."
            execute {
                userTracker.removeUser(author)
                respond("Future game sessions will not be recorded.", ephemeral = true)
            }
        }
    }
