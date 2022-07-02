package com.lurkerbot.web

import com.lurkerbot.core.currentlyPlaying.CurrentlyPlayingService
import com.lurkerbot.core.discordUser.UserService
import com.lurkerbot.core.gameTime.GameTimeService
import com.lurkerbot.core.page.PageService
import com.lurkerbot.mongodb.currentlyPlaying.KMongoCurrentlyPlayingRepository
import com.lurkerbot.mongodb.discordUser.KMongoDiscordUserRepository
import com.lurkerbot.mongodb.gameTime.KMongoTimerRepository
import com.lurkerbot.web.plugins.configureRouting
import com.lurkerbot.web.plugins.configureSerialization
import com.lurkerbot.web.plugins.configureTemplating
import com.lurkerbot.web.routes.configurePagesResource
import com.lurkerbot.web.routes.configureUserResource
import io.ktor.server.application.*
import org.litote.kmongo.KMongo

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress(
    "unused"
) // application.conf references the main function. This annotation prevents the IDE
// from marking it
// as unused.
fun Application.module() {
    val mongoClient = KMongo.createClient(System.getenv("LURKER_SITE_DB"))

    val timerRepository = KMongoTimerRepository(mongoClient)
    val userRepository = KMongoDiscordUserRepository(mongoClient)
    val currentlyPlayingRepository = KMongoCurrentlyPlayingRepository(mongoClient)

    val gameTimerService = GameTimeService(timerRepository)
    val currentlyPlayingService = CurrentlyPlayingService(currentlyPlayingRepository)
    val userService = UserService(userRepository)
    val pageService =
        PageService(userService, gameTimerService, timerRepository, currentlyPlayingService)

    configureRouting()
    configureTemplating()
    configureSerialization()

    configurePagesResource(pageService)
    configureUserResource(gameTimerService)
}
