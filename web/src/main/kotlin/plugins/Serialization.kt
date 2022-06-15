package com.lurkerbot.web.plugins

import gameTime.GameTimeService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(gameTimeService: GameTimeService) {
    install(ContentNegotiation) { json() }

    routing {
        get("/json/kotlinx-serialization") { call.respond(mapOf("hello" to "world")) }
        get("/test") {
            call.respond(gameTimeService.getTimesForDiscordUserById("111703434985529344"))
        }
    }
}
