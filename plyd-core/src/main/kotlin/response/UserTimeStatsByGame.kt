package response

import discordUser.UserInDiscord

@Suppress("DataClassPrivateConstructor")
data class UserTimeStatsByGame
private constructor(
    val userInfo: UserInDiscord,
    val gameTime: List<GameTimeDetailedSum>,
    val byGameState: List<TimeGraphData>,
    val byGameDetail: List<TimeGraphData>,
    val bySmallAsset: List<TimeGraphData>,
    val byLargeAsset: List<TimeGraphData>
) {
    companion object {
        fun of(
            userInfo: UserInDiscord,
            gameTime: List<GameTimeDetailedSum>,
            byGameDetail: List<TimeGraphData>,
            byGameState: List<TimeGraphData>,
            bySmallAsset: List<TimeGraphData>,
            byLargeAsset: List<TimeGraphData>
        ) =
            UserTimeStatsByGame(
                userInfo,
                gameTime,
                byGameState,
                byGameDetail,
                bySmallAsset,
                byLargeAsset
            )
    }
}
