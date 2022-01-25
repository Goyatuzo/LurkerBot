package com.lurkerbot.command

import com.lurkerbot.discordUser.UserTracker
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import mu.KotlinLogging

class RemoveMe(private val userTracker: UserTracker) : BotCommand {
    private val logger = KotlinLogging.logger {}

    override val name: String = "remove-me"
    override val description: String =
        "Stop tracking future game sessions. Will not delete game sessions in the past."
    override suspend fun invoke(interaction: ApplicationCommandInteraction) {
        val user = interaction.user.asUserOrNull()
        if (user != null) {
            userTracker.removeUser(user)

            interaction.acknowledgeEphemeral().apply {
                followUpEphemeral {
                    content =
                        "Future game sessions will not be recorded. Existing records can be deleted with a separate command."
                }
            }
        } else {
            logger.warn { "${interaction.name} was invoked without a user" }
        }
    }
}
