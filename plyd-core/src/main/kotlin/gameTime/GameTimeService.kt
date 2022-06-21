package gameTime

import java.time.LocalDateTime

class GameTimeService(private val timerRepository: TimerRepository) {
    fun getTimesForDiscordUserById(discordUserId: String, from: LocalDateTime) =
        timerRepository.getSummedTimeRecordsFor(discordUserId, from)
}
