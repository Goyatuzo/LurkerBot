package com.lurkerbot.web.plugins

import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureTemplating() {
    install(Pebble) { loader(ClasspathLoader().apply { prefix = "templates" }) }
}
