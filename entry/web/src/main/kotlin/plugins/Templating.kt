package com.lurkerbot.web.plugins

import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.pebbletemplates.pebble.loader.ClasspathLoader

fun Application.configureTemplating() {
    install(Pebble) { loader(ClasspathLoader().apply { prefix = "templates" }) }
}
