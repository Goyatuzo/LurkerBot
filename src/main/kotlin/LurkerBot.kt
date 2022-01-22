package com.lurkerbot

import com.lurkerbot.discordUser.DiscordUserRepository
import com.lurkerbot.discordUser.UserTracker
import com.lurkerbot.gameTime.GameTimeTracker
import com.lurkerbot.gameTime.GameTimer
import com.lurkerbot.gameTime.TimerRepository
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

    val timerRepository = TimerRepository(mongoClient)
    val gameTimer = GameTimer(timerRepository)
    val userRepository = DiscordUserRepository(mongoClient)
    val userTracker = UserTracker(userRepository)
    val gameTimeTracker = GameTimeTracker(gameTimer, userTracker)

    client.on<PresenceUpdateEvent> {
        gameTimeTracker.processEvent(this)
    }

    client.login {
        @OptIn(PrivilegedIntent::class)
        intents = Intents.nonPrivileged + Intent.GuildPresences
    }
}