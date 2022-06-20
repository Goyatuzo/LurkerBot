package com.lurkerbot.web.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import page.PageService

fun Application.configurePagesResource(pageService: PageService) {
    routing {
        get("/") { call.respond(PebbleContent("pages/index.html", emptyMap())) }
        get("/user/{discordUserId}") {
            val userId = call.parameters["discordUserId"]
            val from = call.parameters["from"]

            if (userId.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                val fromDate =
                    if (from == "all") {
                        LocalDateTime.of(0, 1, 1, 0, 0, 0)
                    } else {
                        LocalDateTime.now().minusWeeks(2)
                    }

                val userData = pageService.getUserTimeStatsByDiscordId(userId, fromDate)

                if (userData == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(PebbleContent("pages/user-stats.html", mapOf("user" to userData)))
                }
            }
        }

        get("/user/{discordUserId}/{gameName}") {
            val userId = call.parameters["discordUserId"]
            val gameName = call.parameters["gameName"]
            val from = call.parameters["from"]

            if (userId.isNullOrEmpty() || gameName.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                val fromDate =
                    if (from == "all") {
                        LocalDateTime.of(0, 1, 1, 0, 0, 0)
                    } else {
                        LocalDateTime.now().minusWeeks(2)
                    }

                val userData = pageService.getTimesForDiscordUserByIdAndGame(userId, gameName, fromDate)

                if (userData == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(PebbleContent("pages/user-game-stats.html", mapOf("user" to userData)))
                }
            }
        }
    }
}
