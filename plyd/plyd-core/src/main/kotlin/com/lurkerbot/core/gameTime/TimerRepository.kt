package com.lurkerbot.core.gameTime

import com.lurkerbot.core.response.GameTimeDetailedSum
import com.lurkerbot.core.response.GameTimeSum
import java.time.LocalDateTime

interface TimerRepository {
    fun saveTimeRecord(record: TimeRecord)
    fun getSummedTimeRecordsFor(userId: String, from: LocalDateTime): List<GameTimeSum>
    fun getSummedGameTimeRecordsFor(
        userId: String,
        gameName: String,
        from: LocalDateTime
    ): List<GameTimeDetailedSum>
    fun mostRecentEntries(userId: String, count: Int): List<TimeRecord>
}
