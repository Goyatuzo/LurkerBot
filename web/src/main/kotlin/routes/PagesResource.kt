package com.lurkerbot.web.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import page.PageService

fun Application.configurePagesResource(pageService: PageService) {
    routing {
        get("/") { call.respond(PebbleContent("pages/index.html", emptyMap())) }
        get("/user/{discordUserId}") {
            val userId = call.parameters["discordUserId"]

            if (userId.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                val userData = pageService.getUserTimeStatsByDiscordId(userId)

                if (userData == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(PebbleContent("pages/user-stats.html", mapOf("user" to userData)))
                }
            }
        }
    }
}
