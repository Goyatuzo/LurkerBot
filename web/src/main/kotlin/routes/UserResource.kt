package com.lurkerbot.web.routes

import gameTime.GameTimeService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

fun Application.configureUserResource(gameTimeService: GameTimeService) {
    routing {
        get("/api/user/{discordUserId}") {
            val discordUserId = call.parameters["discordUserId"]

            if (discordUserId == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(
                    gameTimeService.getTimesForDiscordUserById(discordUserId, LocalDateTime.now())
                )
            }
        }

        get("/api/user/{discordUserId}/{gameName}") {
            val discordUserId = call.parameters["discordUserId"]
            val gameName = call.parameters["gameName"]

            if (discordUserId.isNullOrEmpty() || gameName.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {

                val times =
                    gameTimeService.getTimesForDiscordUserByIdAndGame(
                        discordUserId,
                        gameName,
                        LocalDateTime.now().minusWeeks(2)
                    )
                println(times)

                call.respond(
                    gameTimeService.getTimesForDiscordUserByIdAndGame(
                        discordUserId,
                        gameName,
                        LocalDateTime.now().minusWeeks(2)
                    )
                )
            }
        }
    }
}
