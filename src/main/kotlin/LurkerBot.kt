import dev.kord.core.Kord
import dev.kord.core.event.user.PresenceUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import gameTime.GameTimer
import gameTime.TimerRepository
import org.litote.kmongo.KMongo

suspend fun main() {
    val client = Kord(System.getenv("LURKER_BOT_TOKEN"))
    val mongoClient = KMongo.createClient()

    val timerRepository = TimerRepository(mongoClient)
    val gameTimer = GameTimer(timerRepository)

    client.on<PresenceUpdateEvent> {
        println(this)
    }

    client.login {
        @OptIn(PrivilegedIntent::class)
        intents = Intents.nonPrivileged + Intent.GuildPresences
    }
}