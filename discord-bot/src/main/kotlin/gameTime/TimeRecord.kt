package com.lurkerbot.gameTime

import dev.kord.core.entity.Activity
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
        fun fromActivity(userId: String, activity: Activity): TimeRecord =
            TimeRecord(
                sessionBegin = LocalDateTime.now(),
                sessionEnd = LocalDateTime.now(),
                gameName = activity.name,
                userId = userId,
                gameDetail = activity.details,
                gameState = activity.state,
                largeAssetText = activity.assets?.largeText,
                smallAssetText = activity.assets?.smallText
            )
    }
}
