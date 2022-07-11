package com.lurkerbot.core.gameTime

import java.time.LocalDateTime

class TimeSummaryService(private val timerRepository: TimerRepository) {
    fun getAllTimesFromPastWeek() =
        timerRepository.getGroupedTimeRecordsByDate(LocalDateTime.now().minusWeeks(1))
}
