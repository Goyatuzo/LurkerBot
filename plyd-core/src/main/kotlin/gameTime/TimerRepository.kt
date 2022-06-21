package gameTime

import java.time.LocalDateTime
import response.GameTimeDetailedSum
import response.GameTimeSum

interface TimerRepository {
    fun saveTimeRecord(record: TimeRecord)
    fun getSummedTimeRecordsFor(userId: String, from: LocalDateTime): List<GameTimeSum>
    fun getSummedGameTimeRecordsFor(
        userId: String,
        gameName: String,
        from: LocalDateTime
    ): List<GameTimeDetailedSum>
    fun fiveMostRecentEntries(userId: String): List<TimeRecord>
}
