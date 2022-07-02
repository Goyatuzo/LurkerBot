package com.lurkerbot

import com.lurkerbot.command.RegisterCommands
import com.lurkerbot.dependencies.PlydDependencies
import com.lurkerbot.discordUser.UserTracker
import com.lurkerbot.gameTime.GameTimeTracker
import com.lurkerbot.gameTime.GameTimer
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent

suspend fun main() {
    val client = Kord(System.getenv("LURKER_BOT_TOKEN"))

    val dependencies = PlydDependencies()

    val gameTimer = GameTimer(dependencies.timerRepository, dependencies.currentlyPlayingService)
    val userTracker = UserTracker(dependencies.userRepository)
    val gameTimeTracker = GameTimeTracker(gameTimer, userTracker)

    dependencies.currentlyPlayingService.clearAll()

    RegisterCommands(client, userTracker).initialize()

    client.on<PresenceUpdateEvent> { gameTimeTracker.processEvent(this) }

    client.on<ReadyEvent> {
        userTracker.initialize()
        gameTimeTracker.initialize(readyEvent = this)
    }

    @OptIn(PrivilegedIntent::class)
    client.login { intents = Intents.nonPrivileged + Intent.GuildPresences }
}
