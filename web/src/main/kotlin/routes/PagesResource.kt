package com.lurkerbot.web.routes

import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configurePagesResource() {
    routing { get("/") { call.respond(PebbleContent("pages/index.html", emptyMap())) } }
}
