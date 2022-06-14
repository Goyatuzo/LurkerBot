package command

import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import discordUser.UserTracker
import mu.KotlinLogging

class AddMe(private val userTracker: UserTracker) : BotCommand {
    private val logger = KotlinLogging.logger {}

    override val name: String = "add-me"
    override val description: String =
        "Start tracking the time you play games. If you're already tracking, update your username."
    override suspend fun invoke(interaction: ApplicationCommandInteraction) {
        val user = interaction.user.asUserOrNull()
        userTracker.addUser(user)

        interaction.respondEphemeral {
            content = "You are now tracked as **${user.username}#${user.discriminator}**"
        }

        logger.info {
            "${interaction.invokedCommandName} was run for ${user.username}#${user.discriminator}"
        }
    }
}
