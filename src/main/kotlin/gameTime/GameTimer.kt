package gameTime

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

class GameTimer(private val timerRepository: TimerRepository) {
    private val beingTracked: MutableMap<String, TimeRecord> = mutableMapOf()
    private fun generateKey(userId: String, gameName: String) = userId + gameName

    fun beginLogging(userId: String, gameName: String, record: TimeRecord): Result<Unit, GameTimeError> {
        val key = generateKey(userId, gameName)
        if (beingTracked.containsKey(key)) {
            return Err(GameIsAlreadyLogging(userId, gameName, record))
        }
        beingTracked[generateKey(userId, gameName)] = record
        return Ok(Unit)
    }

    fun endLogging(userId: String, gameName: String): Result<TimeRecord, GameTimeError> {
        val key = generateKey(userId, gameName)
        beingTracked[key]?.let {
            timerRepository.saveTimeRecord(it)
            return Ok(it)
        }

        return Err(NeverStartedLogging(userId, gameName))
    }
}