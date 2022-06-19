package com.lurkerbot.web

import com.lurkerbot.web.plugins.configureRouting
import com.lurkerbot.web.plugins.configureSerialization
import com.lurkerbot.web.plugins.configureTemplating
import com.lurkerbot.web.routes.configurePagesResource
import com.lurkerbot.web.routes.configureUserResource
import discordUser.KMongoDiscordUserRepository
import discordUser.UserService
import gameTime.GameTimeService
import gameTime.KMongoTimerRepository
import io.ktor.server.application.*
import org.litote.kmongo.KMongo
import page.PageService

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

    val gameTimerService = GameTimeService(timerRepository)
    val userService = UserService(userRepository)
    val pageService = PageService(userService, gameTimerService)

    configureRouting()
    configureTemplating()
    configureSerialization()

    configurePagesResource(pageService)
    configureUserResource(gameTimerService)
}
