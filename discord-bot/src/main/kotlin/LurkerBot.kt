package com.lurkerbot

import command.RegisterCommands
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import discordUser.KMongoDiscordUserRepository
import discordUser.UserTracker
import org.litote.kmongo.KMongo

suspend fun main() {
    val client = Kord(System.getenv("LURKER_BOT_TOKEN"))
    val mongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))

    val timerRepository = TimerRepository(mongoClient)
    val gameTimer = GameTimer(timerRepository)
    val userRepository = KMongoDiscordUserRepository(mongoClient)
    val userTracker = UserTracker(userRepository)
    val gameTimeTracker = GameTimeTracker(gameTimer, userTracker)

    RegisterCommands(client, userTracker).initialize()

    client.on<PresenceUpdateEvent> { gameTimeTracker.processEvent(this) }

    client.on<ReadyEvent> {
        userTracker.initialize()
        gameTimeTracker.initialize(readyEvent = this)
    }

    @OptIn(PrivilegedIntent::class)
    client.login { intents = Intents.nonPrivileged + Intent.GuildPresences }
}
