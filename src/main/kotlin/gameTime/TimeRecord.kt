package gameTime

import java.time.LocalTime

data class TimeRecord(
    val sessionBegin: LocalTime,
    val sessionEnd: LocalTime,
    val gameName: String,
    val userId: String,
    val gameDetail: String?,
    val gameState: String?,
    val largeAssetText: String?,
    val smallAssetText: String?
)