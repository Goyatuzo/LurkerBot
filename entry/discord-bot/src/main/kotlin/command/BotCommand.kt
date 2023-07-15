package com.lurkerbot.command

import dev.kord.core.entity.interaction.ApplicationCommandInteraction

interface BotCommand {
    val name: String
    val description: String

    suspend fun invoke(interaction: ApplicationCommandInteraction)
}
