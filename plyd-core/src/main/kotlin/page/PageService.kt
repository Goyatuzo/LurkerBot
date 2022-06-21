package page

import discordUser.UserService
import gameTime.GameTimeService
import gameTime.TimerRepository
import java.time.LocalDateTime
import response.UserTimeStats
import response.UserTimeStatsByGame

class PageService(
    private val userService: UserService,
    private val gameTimeService: GameTimeService,
    private val timerRepository: TimerRepository
) {
    fun getUserTimeStatsByDiscordId(userId: String, from: LocalDateTime): UserTimeStats? {
        val userInfo = userService.getUserByDiscordId(userId)
        val gameTimeStats = gameTimeService.getTimesForDiscordUserById(userId, from)
        val mostRecentStats = timerRepository.fiveMostRecentEntries(userId)

        return if (userInfo == null) {
            null
        } else {
            UserTimeStats.of(userInfo, gameTimeStats, mostRecentStats)
        }
    }

    fun getTimesForDiscordUserByIdAndGame(
        userId: String,
        gameName: String,
        from: LocalDateTime
    ): UserTimeStatsByGame? {
        val userInfo = userService.getUserByDiscordId(userId)
        val stats = timerRepository.getSummedGameTimeRecordsFor(userId, gameName, from)

        return if (userInfo == null) {
            null
        } else {
            UserTimeStatsByGame.of(userInfo, stats)
        }
    }
}
