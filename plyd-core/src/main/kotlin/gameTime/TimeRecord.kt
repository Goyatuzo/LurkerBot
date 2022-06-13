package gameTime

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
    companion object { }
}