package com.lurkerbot.command

import dev.kord.core.event.interaction.InteractionCreateEvent

interface BotCommand {
    val name: String
    val description: String
    suspend fun invoke(event: InteractionCreateEvent)
}