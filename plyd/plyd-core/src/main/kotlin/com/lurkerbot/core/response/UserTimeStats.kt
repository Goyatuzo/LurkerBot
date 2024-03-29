package com.lurkerbot.core.response

import com.lurkerbot.core.currentlyPlaying.CurrentlyPlaying
import com.lurkerbot.core.discordUser.UserInDiscord

@Suppress("DataClassPrivateConstructor")
data class UserTimeStats
private constructor(
    val userInfo: UserInDiscord,
    val gameTimeSum: List<GameTimeSum>,
    val mostRecent: List<RecentlyPlayed>,
    val currentlyPlaying: CurrentlyPlaying?
) {
    companion object {
        fun of(
            userInfo: UserInDiscord,
            gameTimeSum: List<GameTimeSum>,
            mostRecent: List<RecentlyPlayed>,
            currentlyPlaying: CurrentlyPlaying?
        ) = UserTimeStats(userInfo, gameTimeSum, mostRecent, currentlyPlaying)
    }
}
