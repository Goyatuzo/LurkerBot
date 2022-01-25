package com.lurkerbot.command

import com.lurkerbot.discordUser.UserTracker
import dev.kord.core.Kord
import dev.kord.core.behavior.createApplicationCommands
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.flow.collectIndexed
import mu.KotlinLogging

class RegisterCommands(private val client: Kord, userTracker: UserTracker) {
    private val logger = KotlinLogging.logger {}

    private val listOfCommands: List<BotCommand> = listOf(AddMeCommand(userTracker))

    suspend fun initialize() {
        client.globalCommands.collectIndexed { _, command ->
            logger.warn("Deleting global command: ${command.name}")
            command.delete()
        }

        client.guilds.collectIndexed { _, guild ->
            guild.commands.collectIndexed { _, command ->
                logger.warn("Deleting guild command: ${command.name} from ${guild.name}")
                command.delete()
            }
        }

        client.guilds.collectIndexed { _, guild ->
            guild.createApplicationCommands {
                listOfCommands.forEach { input(it.name, it.description) }
            }
        }

        client.on<InteractionCreateEvent> {
            when (val interaction = interaction) {
                is ApplicationCommandInteraction ->
                    listOfCommands.forEach { cmd ->
                        if (cmd.name == interaction.name) {
                            cmd.invoke(interaction)
                        }
                    }
                else -> logger.warn { "Command ${interaction::class.java} is not matched" }
            }
        }
    }
}
