package com.lurkerbot.gameTime

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.time.LocalDateTime
import mu.KotlinLogging

class GameTimer(private val timerRepository: TimerRepository) {
    private val logger = KotlinLogging.logger {}

    private val beingTracked: MutableMap<String, TimeRecord> = mutableMapOf()
    private val serverBeingTracked: MutableMap<String, String> = mutableMapOf()

    private fun userIsBeingTracked(userId: String, guildId: String) =
        beingTracked.containsKey(userId) && serverBeingTracked[userId] == guildId

    fun beginLogging(
        userId: String,
        guildId: String,
        record: TimeRecord
    ): Result<Unit, GameTimeError> {
        if (beingTracked.containsKey(userId) && !serverBeingTracked.containsKey(guildId)) {
            return Err(GameIsAlreadyLogging(userId, beingTracked[userId]!!, record))
        }

        logger.info { "Began recording user: $userId in guild: $guildId" }
        beingTracked[userId] = record
        serverBeingTracked[userId] = guildId
        return Ok(Unit)
    }

    fun endLogging(
        userId: String,
        guildId: String,
        at: LocalDateTime = LocalDateTime.now()
    ): Result<TimeRecord, GameTimeError> {
        if (userIsBeingTracked(userId, guildId)) {
            beingTracked[userId]?.let {
                val updatedEnd = it.copy(sessionEnd = at)

                // Remove first to eliminate possibility of data being sent to db
                beingTracked.remove(userId)
                serverBeingTracked.remove(userId)
                timerRepository.saveTimeRecord(updatedEnd)

                return Ok(updatedEnd)
            }
        }

        return Err(NeverStartedLogging(userId, guildId))
    }
}
