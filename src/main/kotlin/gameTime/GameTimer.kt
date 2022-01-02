package gameTime

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.time.LocalTime

class GameTimer(private val timerRepository: TimerRepository) {
    private val beingTracked: MutableMap<String, TimeRecord> = mutableMapOf()

    fun beginLogging(userId: String, record: TimeRecord): Result<Unit, GameTimeError> {
        if (isBeingLogged(userId)) {
            return Err(GameIsAlreadyLogging(userId, beingTracked[userId]!!, record))
        }
        beingTracked[userId] = record
        return Ok(Unit)
    }

    fun endLogging(userId: String, at: LocalTime = LocalTime.now()): Result<TimeRecord, GameTimeError> {
        beingTracked[userId]?.let {
            val updatedEnd = it.copy(sessionEnd = at)
            timerRepository.saveTimeRecord(updatedEnd)
            return Ok(updatedEnd)
        }

        return Err(NeverStartedLogging(userId))
    }

    fun isBeingLogged(userId: String): Boolean = beingTracked.containsKey(userId)
}