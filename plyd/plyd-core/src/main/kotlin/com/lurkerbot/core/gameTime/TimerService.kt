package com.lurkerbot.core.gameTime

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.lurkerbot.core.currentlyPlaying.CurrentlyPlaying
import com.lurkerbot.core.currentlyPlaying.CurrentlyPlayingService
import com.lurkerbot.core.error.DomainError
import com.lurkerbot.core.error.GameIsAlreadyLogging
import com.lurkerbot.core.error.NeverStartedLogging
import com.lurkerbot.core.error.StateChangedTooFast
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import mu.KotlinLogging

class TimerService(
    private val timerRepository: TimerRepository,
    private val currentlyPlayingService: CurrentlyPlayingService
) {
    private val logger = KotlinLogging.logger {}
    fun beginLogging(
        userId: String,
        guildId: String,
        record: TimeRecord
    ): Result<Unit, DomainError> =
        if (currentlyPlayingService.isUserCurrentlyPlayingById(userId)) {
            Err(GameIsAlreadyLogging(userId, record))
        } else {
            val toCurrentlyPlaying = CurrentlyPlaying.from(record, guildId)

            logger.info { "Began recording $toCurrentlyPlaying" }

            currentlyPlayingService.save(toCurrentlyPlaying)
            Ok(Unit)
        }

    fun endLogging(
        userId: String,
        guildId: String,
        at: LocalDateTime = LocalDateTime.now()
    ): Result<Unit, DomainError> =
        currentlyPlayingService.getByUserId(userId).andThen { currentlyPlaying ->
            if (currentlyPlaying.serverId == guildId) {
                val updatedEnd = TimeRecord.from(currentlyPlaying).copy(sessionEnd = at)
                val timeElapsed =
                    ChronoUnit.MILLIS.between(updatedEnd.sessionBegin, updatedEnd.sessionEnd)

                currentlyPlayingService.removeByUserId(userId)
                return if (timeElapsed >= 5000) {
                    // Remove first to eliminate possibility of data being sent to db
                    timerRepository.saveTimeRecord(updatedEnd)

                    Ok(Unit)
                } else {
                    logger.warn {
                        "Not logging for $userId. State change happened in $timeElapsed milliseconds"
                    }
                    Err(StateChangedTooFast(userId, updatedEnd))
                }
            }

            return Err(NeverStartedLogging(userId, guildId))
        }
}
