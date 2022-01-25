package com.lurkerbot.command

import com.lurkerbot.discordUser.UserTracker
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import mu.KotlinLogging

class AddMe(private val userTracker: UserTracker) : BotCommand {
    private val logger = KotlinLogging.logger {}

    override val name: String = "add-me"
    override val description: String = "Start tracking the time you play games. If you're already tracking, update your username."
    override suspend fun invoke(interaction: ApplicationCommandInteraction) {
        val user = interaction.user.asUserOrNull()
        if (user != null) {
            userTracker.addUser(user)

            interaction.acknowledgeEphemeral().apply {
                followUpEphemeral {
                    content = "You are now tracked as **${user.username}#${user.discriminator}**"
                }
            }
        } else {
            logger.warn { "${interaction.name} was invoked without a user" }
        }
    }
}
