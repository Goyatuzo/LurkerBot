package response

import discordUser.UserInDiscord

@Suppress("DataClassPrivateConstructor")
data class UserTimeStats
private constructor(
    val userInfo: UserInDiscord,
    val gameTimeSum: List<GameTimeSum>,
    val mostRecent: List<RecentlyPlayed>
) {
    companion object {
        fun of(
            userInfo: UserInDiscord,
            gameTimeSum: List<GameTimeSum>,
            mostRecent: List<RecentlyPlayed>
        ) = UserTimeStats(userInfo, gameTimeSum, mostRecent)
    }
}
