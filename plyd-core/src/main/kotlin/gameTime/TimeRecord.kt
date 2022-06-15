package gameTime

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
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
    companion object {}
}
