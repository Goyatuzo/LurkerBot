package com.lurkerbot.gameTime

import com.lurkerbot.discordUser.UserTracker
import dev.kord.common.entity.ActivityType
import dev.kord.core.event.user.PresenceUpdateEvent
import java.time.LocalDateTime
import kotlinx.coroutines.sync.Mutex
import mu.KotlinLogging

class GameTimeTracker(private val gameTimer: GameTimer, private val userTracker: UserTracker) {
    private val logger = KotlinLogging.logger {}
    private val mutex = Mutex()

    suspend fun processEvent(event: PresenceUpdateEvent) {
        val user = event.getUser()

        if (!userTracker.userIsBeingTracked(user.id.value.toString())) {
            return
        }

        if (!user.isBot) {
            when (event.presence.activities.size) {
                0 -> {
                    gameTimer.endLogging(user.id.value.toString(), event.guildId.value.toString())
                }
                else -> {
                    val activity =
                        event.presence.activities.firstOrNull { it.type == ActivityType.Game }

                    if (activity != null) {
                        event.old?.activities?.firstOrNull { it.type == ActivityType.Game }?.let {
                            // Check some attributes for equality
                            val sameActivity =
                                it.name == activity.name &&
                                    it.details == activity.details &&
                                    it.assets?.smallText == activity.assets?.smallText &&
                                    it.assets?.largeText == activity.assets?.largeText

                            if (!sameActivity) {
                                gameTimer.endLogging(
                                    user.id.value.toString(),
                                    event.guildId.value.toString()
                                )
                            }
                        }
                        val toRecord =
                            TimeRecord(
                                sessionBegin = LocalDateTime.now(),
                                sessionEnd = LocalDateTime.now(),
                                gameName = activity.name,
                                userId = user.id.value.toString(),
                                gameDetail = activity.details,
                                gameState = activity.state,
                                largeAssetText = activity.assets?.largeText,
                                smallAssetText = activity.assets?.smallText
                            )

                        mutex.lock {
                            gameTimer.beginLogging(
                                user.id.value.toString(),
                                event.guildId.value.toString(),
                                toRecord
                            )
                        }
                    }

                    if (event.presence.activities.size > 1)
                        logger.info { "Multiple activities: ${event.presence.activities}" }
                }
            }
        }
    }
}
