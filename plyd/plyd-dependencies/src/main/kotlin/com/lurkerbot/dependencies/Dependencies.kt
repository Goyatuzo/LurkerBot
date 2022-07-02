package com.lurkerbot.dependencies

import com.lurkerbot.core.currentlyPlaying.CurrentlyPlayingService
import com.lurkerbot.core.discordUser.UserService
import com.lurkerbot.core.gameTime.GameTimeService
import com.lurkerbot.core.page.PageService
import com.lurkerbot.mongodb.currentlyPlaying.KMongoCurrentlyPlayingRepository
import com.lurkerbot.mongodb.discordUser.KMongoDiscordUserRepository
import com.lurkerbot.mongodb.gameTime.KMongoTimerRepository

class Dependencies() {
    val mongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))
    val timerRepository = KMongoTimerRepository(mongoClient)
    val userRepository = KMongoDiscordUserRepository(mongoClient)
    val currentlyPlayingRepository = KMongoCurrentlyPlayingRepository(mongoClient)

    val gameTimerService = GameTimeService(timerRepository)
    val currentlyPlayingService = CurrentlyPlayingService(currentlyPlayingRepository)
    val userService = UserService(userRepository)
    val pageService =
        PageService(userService, gameTimerService, timerRepository, currentlyPlayingService)
}