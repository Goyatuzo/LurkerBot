package com.lurkerbot.web

import com.lurkerbot.dependencies.PlydDependencies
import com.lurkerbot.web.plugins.configureRouting
import com.lurkerbot.web.plugins.configureSerialization
import com.lurkerbot.web.plugins.configureTemplating
import com.lurkerbot.web.routes.configurePagesResource
import com.lurkerbot.web.routes.configureSiteStatisticsResource
import com.lurkerbot.web.routes.configureUserResource
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress(
    "unused"
) // application.conf references the main function. This annotation prevents the IDE
// from marking it
// as unused.
fun Application.module() {
    val dependencies = PlydDependencies()

    configureRouting()
    configureTemplating()
    configureSerialization()

    configurePagesResource(dependencies.pageService)
    configureUserResource(dependencies.gameTimerService)
    configureSiteStatisticsResource(dependencies.timeSummaryService)
}
