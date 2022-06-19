package page

import discordUser.UserService
import gameTime.GameTimeService
import response.UserTimeStats
import java.time.LocalDateTime

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
}
