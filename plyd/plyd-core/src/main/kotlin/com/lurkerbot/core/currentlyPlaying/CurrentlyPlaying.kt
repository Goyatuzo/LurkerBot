package com.lurkerbot.core.currentlyPlaying

import com.lurkerbot.core.gameTime.TimeRecord
import java.time.LocalDateTime

data class CurrentlyPlaying
internal constructor(
    val sessionBegin: LocalDateTime,
    val gameName: String,
    val userId: String,
    val serverId: String,
    val gameDetail: String?,
    val gameState: String?,
    val largeAssetText: String?,
    val smallAssetText: String?
) {
    companion object {
        fun from(timeRecord: TimeRecord, serverId: String): CurrentlyPlaying =
            CurrentlyPlaying(
                userId = timeRecord.userId,
                serverId = serverId,
                gameName = timeRecord.gameName,
                gameDetail = timeRecord.gameDetail,
                gameState = timeRecord.gameState,
                smallAssetText = timeRecord.smallAssetText,
                largeAssetText = timeRecord.largeAssetText,
                sessionBegin = timeRecord.sessionBegin
            )
    }
}
