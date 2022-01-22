package com.lurkerbot.gameTime

import com.lurkerbot.discordUser.UserTracker
import dev.kord.common.entity.ActivityType
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import kotlinx.coroutines.flow.collect
import mu.KotlinLogging

class GameTimeTracker(private val gameTimer: GameTimer, private val userTracker: UserTracker) {
    private val logger = KotlinLogging.logger {}

    suspend fun onReady(readyEvent: ReadyEvent) {
        val guilds = readyEvent.getGuilds()

        guilds.collect { guild ->
            guild.members.collect { member ->
                if (userTracker.userIsBeingTracked(member.id.toString())) {
                    val presence = member.getPresenceOrNull()

                    if (presence != null) {
                        val gameActivity =
                            presence.activities.firstOrNull { it.type == ActivityType.Game }

                        if (gameActivity != null) {
                            val toRecord =
                                TimeRecord.fromActivity(member.id.toString(), gameActivity)

                            gameTimer.beginLogging(
                                member.id.toString(),
                                guild.id.toString(),
                                toRecord
                            )
                        }
                    }
                }
            }
        }
    }

    suspend fun processEvent(event: PresenceUpdateEvent) {
        if (!userTracker.userIsBeingTracked(event.user.id.toString())) {
            return
        }

        val user = event.getUser()

        if (!user.isBot) {
            val currentGame = event.presence.activities.firstOrNull { it.type == ActivityType.Game }
            val oldGame = event.old?.activities?.firstOrNull { it.type == ActivityType.Game }

            if (currentGame == null || !(currentGame.sameActivityAs(oldGame))) {
                gameTimer.endLogging(user.id.value.toString(), event.guildId.value.toString())
            }

            if (currentGame != null) {
                val toRecord = TimeRecord.fromActivity(user.id.value.toString(), currentGame)

                gameTimer.beginLogging(
                    user.id.value.toString(),
                    event.guildId.value.toString(),
                    toRecord
                )
            }

            if (event.presence.activities.size > 1)
                logger.info { "Multiple activities: ${event.presence.activities}" }
        } else {
            logger.warn { "A bot was about to get recorded" }
        }
    }
}
