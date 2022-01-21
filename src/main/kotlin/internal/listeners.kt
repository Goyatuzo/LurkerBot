package com.lurkerbot.internal

import dev.kord.core.event.gateway.DisconnectEvent
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import me.jakejmattson.discordkt.dsl.listeners
import mu.KotlinLogging

@Suppress("unused")
fun botListeners() = listeners {
    val logger = KotlinLogging.logger {}

    val gameTimeTracker = Dependencies.gameTimeTracker

    on<PresenceUpdateEvent> { gameTimeTracker.processEvent(this) }

    on<ReadyEvent> {
        if (!logger.isInfoEnabled) {
            println("Logger levels: WARN - ${logger.isWarnEnabled}, INFO - ${logger.isInfoEnabled}")
        }
    }

    on<DisconnectEvent> {
        logger.info { "Disconnected" }
        logger.warn { "$this" }
    }
}
