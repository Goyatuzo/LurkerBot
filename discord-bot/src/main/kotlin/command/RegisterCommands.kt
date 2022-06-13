package command

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

    private val listOfCommands: List<BotCommand> = listOf(AddMe(userTracker), RemoveMe(userTracker))

    suspend fun initialize() {
        client.getGlobalApplicationCommands().collectIndexed { _, command ->
            logger.warn("Deleting global command: ${command.name}")
            command.delete()
        }

        client.guilds.collectIndexed { _, guild ->
            guild.getApplicationCommands().collectIndexed { _, command ->
                logger.warn("Deleting guild command: ${command.name} from ${guild.name}")
                command.delete()
            }
        }

        if (System.getenv("env") == "PRODUCTION") {
            client.createGlobalApplicationCommands {
                listOfCommands.forEach {
                    input(it.name, it.description)
                    logger.info { "Created global command ${it.name}" }
                }
            }
        } else {
            client.guilds.collectIndexed { _, guild ->
                guild.createApplicationCommands {
                    listOfCommands.forEach {
                        input(it.name, it.description)
                        logger.info { "Created guild command ${it.name} in ${guild.name}" }
                    }
                }
            }
        }

        client.on<InteractionCreateEvent> {
            when (val interaction = interaction) {
                is ApplicationCommandInteraction ->
                    listOfCommands.forEach { cmd ->
                        if (cmd.name == interaction.invokedCommandName) {
                            cmd.invoke(interaction)
                        }
                    }
                else -> logger.warn { "Command ${interaction::class.java} is not matched" }
            }
        }
    }
}
