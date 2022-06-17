package com.lurkerbot.gameTime

import dev.kord.core.entity.Activity
import gameTime.TimeRecord
import java.time.LocalDateTime

fun TimeRecord.Companion.fromActivity(userId: String, activity: Activity): TimeRecord =
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
