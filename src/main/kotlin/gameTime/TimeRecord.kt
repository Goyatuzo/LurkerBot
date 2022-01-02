package gameTime

import java.time.LocalDate

data class TimeRecord(
    val sessionBegin: LocalDate,
    val sessionEnd: LocalDate,
    val gameName: String,
    val userId: String,
    val gameDetail: String,
    val gameState: String,
    val largeAssetText: String,
    val smallAssetText: String,
    val gameType: String
)