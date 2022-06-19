package response

import discordUser.UserInDiscord

@Suppress("DataClassPrivateConstructor")
data class UserTimeStatsByGame
private constructor(val userInfo: UserInDiscord, val gameTime: List<GameTimeDetailedSum>) {
    companion object {
        fun of(userInfo: UserInDiscord, gameTime: List<GameTimeDetailedSum>) =
            UserTimeStatsByGame(userInfo, gameTime)
    }
}
