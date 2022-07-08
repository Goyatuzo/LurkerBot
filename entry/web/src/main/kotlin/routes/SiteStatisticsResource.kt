package com.lurkerbot.web.routes

import com.lurkerbot.core.gameTime.TimeSummaryService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSiteStatisticsResource(timeSummaryService: TimeSummaryService) {
    routing {
        get("/api/past-week") { call.respond(timeSummaryService.getTopFivePlayedGames()) }
    }
}
