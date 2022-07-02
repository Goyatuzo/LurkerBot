package com.lurkerbot

import com.lurkerbot.command.RegisterCommands
import com.lurkerbot.core.currentlyPlaying.CurrentlyPlayingService
import com.lurkerbot.discordUser.UserTracker
import com.lurkerbot.gameTime.GameTimeTracker
import com.lurkerbot.gameTime.GameTimer
import com.lurkerbot.mongodb.currentlyPlaying.KMongoCurrentlyPlayingRepository
import com.lurkerbot.mongodb.discordUser.KMongoDiscordUserRepository
import com.lurkerbot.mongodb.gameTime.KMongoTimerRepository
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import org.litote.kmongo.KMongo

suspend fun main() {
    val client = Kord(System.getenv("LURKER_BOT_TOKEN"))
    val mongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))

    val timerRepository = KMongoTimerRepository(mongoClient)
    val userRepository = KMongoDiscordUserRepository(mongoClient)
    val currentlyPlayingRepository = KMongoCurrentlyPlayingRepository(mongoClient)

    val currentlyPlayingService = CurrentlyPlayingService(currentlyPlayingRepository)

    val gameTimer = GameTimer(timerRepository, currentlyPlayingService)
    val userTracker = UserTracker(userRepository)
    val gameTimeTracker = GameTimeTracker(gameTimer, userTracker)

    currentlyPlayingService.clearAll()

    RegisterCommands(client, userTracker).initialize()

    client.on<PresenceUpdateEvent> { gameTimeTracker.processEvent(this) }

    client.on<ReadyEvent> {
        userTracker.initialize()
        gameTimeTracker.initialize(readyEvent = this)
    }

    @OptIn(PrivilegedIntent::class)
    client.login { intents = Intents.nonPrivileged + Intent.GuildPresences }
}
