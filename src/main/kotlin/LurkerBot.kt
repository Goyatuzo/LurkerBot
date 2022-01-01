import dev.kord.core.Kord
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import kotlinx.coroutines.delay

suspend fun main() {
    val client = Kord(System.getenv("LURKER_BOT_TOKEN"))

    client.on<PresenceUpdateEvent> {
        println(this)
    }

    client.login {
        @OptIn(PrivilegedIntent::class)
        intents = Intents.nonPrivileged + Intent.GuildPresences
    }
}