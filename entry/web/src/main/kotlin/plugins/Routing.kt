package com.lurkerbot.web.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Routing)

    install(StatusPages) {
        exception<AuthenticationException> { call, _ -> call.respond(HttpStatusCode.Unauthorized) }
        exception<AuthorizationException> { call, _ -> call.respond(HttpStatusCode.Forbidden) }
    }

    routing { static("/static") { resources("static") } }
}

class AuthenticationException : RuntimeException()

class AuthorizationException : RuntimeException()
