package com.lurkerbot.gameTime

import com.lurkerbot.core.gameTime.TimeRecord
import com.lurkerbot.core.gameTime.TimerService
import com.lurkerbot.discordUser.UserTracker
import dev.kord.common.entity.ActivityType
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import kotlinx.coroutines.flow.collectIndexed
import mu.KotlinLogging

class GameTimeTracker(
    private val timerService: TimerService,
    private val userTracker: UserTracker
) {
    private val logger = KotlinLogging.logger {}

    suspend fun initialize(readyEvent: ReadyEvent) {
        val guilds = readyEvent.getGuilds()

        guilds.collectIndexed { _, guild ->
            guild.members.collectIndexed { _, member ->
                if (userTracker.userIsBeingTracked(member.id.toString())) {
                    val gameActivity =
                        member.getPresenceOrNull()?.activities?.firstOrNull {
                            it.type == ActivityType.Game
                        }

                    if (gameActivity != null) {
                        val toRecord = TimeRecord.fromActivity(member.id.toString(), gameActivity)

                        timerService.beginLogging(
                            member.id.toString(),
                            guild.id.toString(),
                            toRecord
                        )
                    }
                }
            }
        }
    }

    suspend fun processEvent(event: PresenceUpdateEvent) {
        val user = event.getUser()

        if (!userTracker.userIsBeingTracked(user.id.value.toString())) {
            return
        }

        if (!user.isBot) {
            val currentGame = event.presence.activities.firstOrNull { it.type == ActivityType.Game }
            val oldGame = event.old?.activities?.firstOrNull { it.type == ActivityType.Game }

            if (currentGame == null || !(currentGame.sameActivityAs(oldGame))) {
                timerService.endLogging(user.id.value.toString(), event.guildId.value.toString())
            }

            if (currentGame != null) {
                val toRecord = TimeRecord.fromActivity(user.id.value.toString(), currentGame)

                timerService.beginLogging(
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
