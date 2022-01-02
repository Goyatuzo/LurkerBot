import dev.kord.common.entity.ActivityType
import dev.kord.core.event.user.PresenceUpdateEvent
import gameTime.GameTimer
import gameTime.TimeRecord
import gameTime.TimerRepository
import java.time.LocalTime

class GameTimeTracker(private val gameTimer: GameTimer) {
    suspend fun processEvent(event: PresenceUpdateEvent) {
        val user = event.getUser()
        if (!user.isBot) {
            when (event.presence.activities.size) {
                0 -> {
                    println("Done")
                }
                else -> {
                    val activity = event.presence.activities.first { it.type == ActivityType.Game }
                    val toRecord = TimeRecord(
                        sessionBegin = LocalTime.now(),
                        sessionEnd = LocalTime.now(),
                        gameName = activity.name,
                        userId = user.id.value.toString(),
                        gameDetail = activity.details.orEmpty(),
                        gameState = activity.state.orEmpty(),
                        largeAssetText = activity.assets?.largeText.orEmpty(),
                        smallAssetText = activity.assets?.smallText.orEmpty()
                    )

                    println(toRecord)

                    if (event.presence.activities.size > 1) println(event.presence.activities)
                }
            }
        }

        println()
    }
}
