package com.lurkerbot.command

import com.lurkerbot.discordUser.UserTracker
import dev.kord.core.event.interaction.InteractionCreateEvent
import mu.KotlinLogging

class AddMeCommand(private val userTracker: UserTracker) : BotCommand {
    private val logger = KotlinLogging.logger {}

    override val name: String = "add-me"
    override val description: String = "Descriptive"
    override suspend fun invoke(event: InteractionCreateEvent) {
        val user = event.interaction.user.asUserOrNull()
        if (user != null) {
            userTracker.addUser(user)
        } else {
            logger.warn { "${event.interaction.data.data.name} was invoked without a user" }
        }
    }
}
