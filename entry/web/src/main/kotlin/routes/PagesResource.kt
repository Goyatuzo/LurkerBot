package com.lurkerbot.web.routes

import com.github.michaelbull.result.mapBoth
import com.lurkerbot.core.page.PageService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

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

                pageService
                    .getUserTimeStatsByDiscordId(userId, fromDate)
                    .mapBoth(
                        {
                            call.respond(
                                PebbleContent("pages/user-stats.html", mapOf("user" to it))
                            )
                        },
                        { call.respond(HttpStatusCode.NotFound) }
                    )
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

                pageService
                    .getTimesForDiscordUserByIdAndGame(userId, gameName, fromDate)
                    .mapBoth(
                        {
                            call.respond(
                                PebbleContent("pages/user-game-stats.html", mapOf("user" to it))
                            )
                        },
                        { call.respond(HttpStatusCode.NotFound) }
                    )
            }
        }
    }
}
