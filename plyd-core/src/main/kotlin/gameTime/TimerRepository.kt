package gameTime

import java.time.LocalDateTime
import response.GameTimeSum

interface TimerRepository {
    fun saveTimeRecord(record: TimeRecord)
    fun getSummedTimeRecordsFor(
        userId: String,
        from: LocalDateTime = LocalDateTime.now().minusWeeks(2)
    ): List<GameTimeSum>
}
