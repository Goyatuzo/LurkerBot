package com.lurkerbot.core.gameTime

import java.time.LocalDateTime

class TimeSummaryService(private val timerRepository: TimerRepository) {
    fun getTopFivePlayedGames() = timerRepository.getGroupedTimeRecordsByDate(LocalDateTime.now().minusWeeks(1), 5)
}