package com.lurkerbot.gameTime

import com.lurkerbot.discordUser.UserTracker
import dev.kord.common.entity.ActivityType
import dev.kord.core.event.user.PresenceUpdateEvent
import dev.kord.core.kordLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.time.LocalTime

class GameTimeTracker(
    private val gameTimer: GameTimer,
    private val userTracker: UserTracker
) {
    private val logger = KotlinLogging.logger {}

    suspend fun processEvent(event: PresenceUpdateEvent) {
        val user = event.getUser()

        if (!userTracker.userIsBeingTracked(user.id.value.toString())) {
//            logger.info { "Not tracked: $user" }
            return
        }

        if (!user.isBot) {
            when (event.presence.activities.size) {
                0 -> {
                    gameTimer.endLogging(user.id.value.toString())
                }
                else -> {
                    val activity = event.presence.activities.firstOrNull { it.type == ActivityType.Game }

                    if (activity != null) {
                        val oldActivity = event.old?.activities?.firstOrNull { it.type == ActivityType.Game }
                        if (oldActivity?.equals(activity) != true) {
                            logger.info { "Current: $activity" }
                            logger.info { "Before: $oldActivity"}
                            gameTimer.endLogging(user.id.value.toString())
                        }

                        val toRecord = TimeRecord(
                            sessionBegin = LocalDateTime.now(),
                            sessionEnd = LocalDateTime.now(),
                            gameName = activity.name,
                            userId = user.id.value.toString(),
                            gameDetail = activity.details,
                            gameState = activity.state,
                            largeAssetText = activity.assets?.largeText,
                            smallAssetText = activity.assets?.smallText
                        )

                        gameTimer.beginLogging(user.id.value.toString(), toRecord)
                    }

                    if (event.presence.activities.size > 1) logger.info { (event.presence.activities) }
                }
            }
        }
    }
}
