package gameTime

import dev.kord.common.entity.ActivityType
import dev.kord.core.event.user.PresenceUpdateEvent
import java.time.LocalTime

class GameTimeTracker(private val gameTimer: GameTimer) {
    suspend fun processEvent(event: PresenceUpdateEvent) {
        val user = event.getUser()
        if (!user.isBot) {
            when (event.presence.activities.size) {
                0 -> {
                    gameTimer.endLogging(user.id.value.toString())
                }
                else -> {
                    val activity = event.presence.activities.firstOrNull { it.type == ActivityType.Game }

                    if (activity != null) {
                        if (event.old?.activities?.firstOrNull { it.type == ActivityType.Game }?.equals(activity) != true) {
                            gameTimer.endLogging(user.id.value.toString())
                        }

                        val toRecord = TimeRecord(
                            sessionBegin = LocalTime.now(),
                            sessionEnd = LocalTime.now(),
                            gameName = activity.name,
                            userId = user.id.value.toString(),
                            gameDetail = activity.details,
                            gameState = activity.state,
                            largeAssetText = activity.assets?.largeText,
                            smallAssetText = activity.assets?.smallText
                        )

                        gameTimer.beginLogging(user.id.value.toString(), toRecord)
                    }

                    if (event.presence.activities.size > 1) println(event.presence.activities)
                }
            }
        }

        println()
    }
}
