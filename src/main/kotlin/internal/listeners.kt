package com.lurkerbot.internal

import com.lurkerbot.discordUser.DiscordUserRepository
import com.lurkerbot.discordUser.UserTracker
import com.lurkerbot.gameTime.GameTimeTracker
import com.lurkerbot.gameTime.GameTimer
import com.lurkerbot.gameTime.TimerRepository
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import me.jakejmattson.discordkt.api.dsl.listeners
import mu.KotlinLogging
import org.litote.kmongo.KMongo

@Suppress("unused")
fun botListeners() = listeners {
    val logger = KotlinLogging.logger {}
    val mongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))

    val timerRepository = TimerRepository(mongoClient)
    val gameTimer = GameTimer(timerRepository)
    val userRepository = DiscordUserRepository(mongoClient)
    val userTracker = UserTracker(userRepository)
    val gameTimeTracker = GameTimeTracker(gameTimer, userTracker)

    on<PresenceUpdateEvent> { gameTimeTracker.processEvent(this) }

    on<ReadyEvent> {
        if (!logger.isInfoEnabled) {
            println("Logger levels: WARN - ${logger.isWarnEnabled}, INFO - ${logger.isInfoEnabled}")
        }
    }
}
