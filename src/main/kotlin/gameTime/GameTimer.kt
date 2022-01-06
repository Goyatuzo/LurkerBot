package com.lurkerbot.gameTime

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.time.LocalDateTime

class GameTimer(private val timerRepository: TimerRepository) {
    private val beingTracked: MutableMap<String, TimeRecord> = mutableMapOf()
    private val serverBeingTracked: MutableMap<String, String> = mutableMapOf()

    private fun userIsBeingTracked(userId: String) =
        beingTracked.containsKey(userId) && serverBeingTracked.containsKey(userId)

    fun beginLogging(
        userId: String,
        guildId: String,
        record: TimeRecord
    ): Result<Unit, GameTimeError> {
        if (userIsBeingTracked(userId)) {
            return Err(GameIsAlreadyLogging(userId, beingTracked[userId]!!, record))
        }
        beingTracked[userId] = record
        serverBeingTracked[userId] = guildId
        return Ok(Unit)
    }

    fun endLogging(
        userId: String,
        guildId: String,
        at: LocalDateTime = LocalDateTime.now()
    ): Result<TimeRecord, GameTimeError> {
        if (userIsBeingTracked(userId)) {
            beingTracked[userId]?.let {
                val updatedEnd = it.copy(sessionEnd = at)
                timerRepository.saveTimeRecord(updatedEnd)
                beingTracked.remove(userId)
                serverBeingTracked.remove(userId)

                return Ok(updatedEnd)
            }
        }

        return Err(NeverStartedLogging(userId, guildId))
    }
}
