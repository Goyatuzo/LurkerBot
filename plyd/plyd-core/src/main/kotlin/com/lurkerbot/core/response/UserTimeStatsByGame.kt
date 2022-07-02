package com.lurkerbot.core.response

import com.lurkerbot.core.discordUser.UserInDiscord

@Suppress("DataClassPrivateConstructor")
data class UserTimeStatsByGame
private constructor(
    val userInfo: UserInDiscord,
    val gameName: String,
    val gameTime: List<GameTimeDetailedSum>,
    val byGameState: List<TimeGraphData>,
    val byGameDetail: List<TimeGraphData>,
    val bySmallAsset: List<TimeGraphData>,
    val byLargeAsset: List<TimeGraphData>
) {
    companion object {
        fun of(
            userInfo: UserInDiscord,
            gameName: String,
            gameTime: List<GameTimeDetailedSum>,
            byGameDetail: List<TimeGraphData>,
            byGameState: List<TimeGraphData>,
            bySmallAsset: List<TimeGraphData>,
            byLargeAsset: List<TimeGraphData>
        ) =
            UserTimeStatsByGame(
                userInfo,
                gameName,
                gameTime,
                byGameState,
                byGameDetail,
                bySmallAsset,
                byLargeAsset
            )
    }
}
