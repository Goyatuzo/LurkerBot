package com.lurkerbot.gameTime

import com.lurkerbot.discordUser.UserTracker
import dev.kord.common.entity.ActivityType
import dev.kord.core.event.user.PresenceUpdateEvent
import java.time.LocalDateTime
import mu.KotlinLogging

class GameTimeTracker(private val gameTimer: GameTimer, private val userTracker: UserTracker) {
    private val logger = KotlinLogging.logger {}

    suspend fun processEvent(event: PresenceUpdateEvent) {
        val user = event.getUser()

        if (!userTracker.userIsBeingTracked(user.id.value.toString())) {
            return
        }

        if (!user.isBot) {
            val currentGame = event.presence.activities.firstOrNull { it.type == ActivityType.Game }
            val oldGame = event.old?.activities?.firstOrNull { it.type == ActivityType.Game }

            if (currentGame == null || !(currentGame.sameActivityAs(oldGame))) {
                gameTimer.endLogging(user.id.value.toString(), event.guildId.value.toString())
            }

            if (currentGame != null) {
                val toRecord =
                    TimeRecord(
                        sessionBegin = LocalDateTime.now(),
                        sessionEnd = LocalDateTime.now(),
                        gameName = currentGame.name,
                        userId = user.id.value.toString(),
                        gameDetail = currentGame.details,
                        gameState = currentGame.state,
                        largeAssetText = currentGame.assets?.largeText,
                        smallAssetText = currentGame.assets?.smallText
                    )

                gameTimer.beginLogging(
                    user.id.value.toString(),
                    event.guildId.value.toString(),
                    toRecord
                )
            }

            if (event.presence.activities.size > 1)
                logger.info { "Multiple activities: ${event.presence.activities}" }
        }
    }
}
