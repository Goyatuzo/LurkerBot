package com.lurkerbot.internal

import com.lurkerbot.discordUser.DiscordUserRepository
import com.lurkerbot.discordUser.UserTracker
import com.lurkerbot.gameTime.GameTimeTracker
import com.lurkerbot.gameTime.GameTimer
import com.lurkerbot.gameTime.TimerRepository

object Dependencies {
    private val timerRepository = TimerRepository(MongoDatabase.client)
    private val userRepository = DiscordUserRepository(MongoDatabase.client)
    private val gameTimer = GameTimer(timerRepository)
    val userTracker = UserTracker(userRepository)
    val gameTimeTracker = GameTimeTracker(gameTimer, userTracker)
}
