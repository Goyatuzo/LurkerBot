package command

import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import discordUser.UserTracker
import mu.KotlinLogging

class RemoveMe(private val userTracker: UserTracker) : BotCommand {
    private val logger = KotlinLogging.logger {}

    override val name: String = "remove-me"
    override val description: String =
        "Stop tracking future game sessions. Will not delete game sessions in the past."
    override suspend fun invoke(interaction: ApplicationCommandInteraction) {
        val user = interaction.user.asUserOrNull()
        userTracker.removeUser(user)

        interaction.respondEphemeral {
            content =
                "Future game sessions will not be recorded. Existing records can be deleted with a separate command."
        }

        logger.info {
            "${interaction.invokedCommandName} was run for ${user.username}#${user.discriminator}"
        }
    }
}
