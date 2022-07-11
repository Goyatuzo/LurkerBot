package com.lurkerbot.dependencies

import com.lurkerbot.core.currentlyPlaying.CurrentlyPlayingRepository
import com.lurkerbot.core.currentlyPlaying.CurrentlyPlayingService
import com.lurkerbot.core.discordUser.DiscordUserRepository
import com.lurkerbot.core.discordUser.UserService
import com.lurkerbot.core.gameTime.GameTimeService
import com.lurkerbot.core.gameTime.TimeSummaryService
import com.lurkerbot.core.gameTime.TimerRepository
import com.lurkerbot.core.page.PageService
import com.lurkerbot.mongodb.currentlyPlaying.KMongoCurrentlyPlayingRepository
import com.lurkerbot.mongodb.discordUser.KMongoDiscordUserRepository
import com.lurkerbot.mongodb.gameTime.KMongoTimerRepository
import org.litote.kmongo.KMongo

class PlydDependencies() {
    private val mongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))
    val timerRepository: TimerRepository = KMongoTimerRepository(mongoClient)
    val userRepository: DiscordUserRepository = KMongoDiscordUserRepository(mongoClient)
    val currentlyPlayingRepository: CurrentlyPlayingRepository =
        KMongoCurrentlyPlayingRepository(mongoClient)

    val gameTimerService = GameTimeService(timerRepository)
    val currentlyPlayingService = CurrentlyPlayingService(currentlyPlayingRepository)
    val userService = UserService(userRepository)
    val timeSummaryService = TimeSummaryService(timerRepository)

    val pageService =
        PageService(
            userService = userService,
            gameTimeService = gameTimerService,
            timerRepository = timerRepository,
            timeSummaryService = timeSummaryService,
            currentlyPlayingService = currentlyPlayingService
        )
}
