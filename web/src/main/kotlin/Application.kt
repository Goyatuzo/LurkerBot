package com.lurkerbot.web

import com.lurkerbot.web.plugins.configureRouting
import com.lurkerbot.web.plugins.configureSerialization
import com.lurkerbot.web.plugins.configureTemplating
import gameTime.GameTimeService
import gameTime.KMongoTimerRepository
import io.ktor.server.application.*
import org.litote.kmongo.KMongo

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress(
    "unused"
) // application.conf references the main function. This annotation prevents the IDE
// from marking it
// as unused.
fun Application.module() {
    val mongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))

    val timerRepository = KMongoTimerRepository(mongoClient)

    val gameTimerService = GameTimeService(timerRepository)

    configureRouting()
    configureTemplating()
    configureSerialization(gameTimerService)
}
