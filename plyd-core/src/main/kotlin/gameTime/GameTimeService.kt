package gameTime

import java.time.LocalDateTime

class GameTimeService(private val timerRepository: TimerRepository) {
    fun getTimesForDiscordUserById(discordUserId: String, from: LocalDateTime = LocalDateTime.now().minusWeeks(2)) =
        timerRepository.getSummedTimeRecordsFor(discordUserId, from)
}
