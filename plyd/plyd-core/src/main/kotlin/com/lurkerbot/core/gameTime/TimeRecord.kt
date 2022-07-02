package com.lurkerbot.core.gameTime

import com.lurkerbot.core.currentlyPlaying.CurrentlyPlaying
import java.time.LocalDateTime

data class TimeRecord(
    val sessionBegin: LocalDateTime,
    val sessionEnd: LocalDateTime,
    val gameName: String,
    val userId: String,
    val gameDetail: String?,
    val gameState: String?,
    val largeAssetText: String?,
    val smallAssetText: String?
) {
    companion object {
        fun from(currentlyPlaying: CurrentlyPlaying) =
            TimeRecord(
                userId = currentlyPlaying.userId,
                gameName = currentlyPlaying.gameName,
                gameDetail = currentlyPlaying.gameDetail,
                gameState = currentlyPlaying.gameState,
                smallAssetText = currentlyPlaying.smallAssetText,
                largeAssetText = currentlyPlaying.largeAssetText,
                sessionBegin = currentlyPlaying.sessionBegin,
                sessionEnd = LocalDateTime.now()
            )
    }
}
