package gameTime

class GameTimeService(private val timerRepository: TimerRepository) {
    fun getTimesForDiscordUserById(discordUserId: String) =
        timerRepository.getSummedTimeRecordsFor(discordUserId)
}
