package response

import discordUser.UserInDiscord
import gameTime.TimeRecord

@Suppress("DataClassPrivateConstructor")
data class UserTimeStats
private constructor(
    val userInfo: UserInDiscord,
    val gameTimeSum: List<GameTimeSum>,
    val mostRecent: List<TimeRecord>
) {
    companion object {
        fun of(
            userInfo: UserInDiscord,
            gameTimeSum: List<GameTimeSum>,
            mostRecent: List<TimeRecord>
        ) = UserTimeStats(userInfo, gameTimeSum, mostRecent)
    }
}
