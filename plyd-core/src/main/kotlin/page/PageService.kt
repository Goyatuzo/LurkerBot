package page

import discordUser.UserService
import gameTime.GameTimeService
import response.UserTimeStats

class PageService(
    private val userService: UserService,
    private val gameTimeService: GameTimeService
) {
    fun getUserTimeStatsByDiscordId(userId: String): UserTimeStats? {
        val userInfo = userService.getUserByDiscordId(userId)
        val gameTimeStats = gameTimeService.getTimesForDiscordUserById(userId)

        return if (userInfo == null) {
            null
        } else {
            UserTimeStats.of(userInfo, gameTimeStats)
        }
    }
}
