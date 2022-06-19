package response

import discordUser.UserInDiscord

@Suppress("DataClassPrivateConstructor")
data class UserTimeStats
private constructor(val userInfo: UserInDiscord, val gameTimeSum: List<GameTimeSum>) {
    companion object {
        fun of(userInfo: UserInDiscord, gameTimeSum: List<GameTimeSum>) =
            UserTimeStats(userInfo, gameTimeSum)
    }
}
