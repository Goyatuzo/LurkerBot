package page

import discordUser.UserService
import gameTime.GameTimeService
import java.time.LocalDateTime
import response.UserTimeStats
import response.UserTimeStatsByGame

class PageService(
    private val userService: UserService,
    private val gameTimeService: GameTimeService
) {
    fun getUserTimeStatsByDiscordId(userId: String, from: LocalDateTime): UserTimeStats? {
        val userInfo = userService.getUserByDiscordId(userId)
        val gameTimeStats = gameTimeService.getTimesForDiscordUserById(userId, from)

        return if (userInfo == null) {
            null
        } else {
            UserTimeStats.of(userInfo, gameTimeStats)
        }
    }

    fun getTimesForDiscordUserByIdAndGame(
        userId: String,
        gameName: String,
        from: LocalDateTime
    ): UserTimeStatsByGame? {
        val userInfo = userService.getUserByDiscordId(userId)
        val stats = gameTimeService.getTimesForDiscordUserByIdAndGame(userId, gameName, from)

        return if (userInfo == null) {
            null
        } else {
            UserTimeStatsByGame.of(userInfo, stats)
        }
    }
}
