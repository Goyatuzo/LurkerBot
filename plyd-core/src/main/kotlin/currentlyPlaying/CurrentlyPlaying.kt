package currentlyPlaying

import gameTime.TimeRecord
import java.time.LocalDateTime

data class CurrentlyPlaying
internal constructor(
    val sessionBegin: LocalDateTime,
    val gameName: String,
    val userId: String,
    val gameDetail: String?,
    val gameState: String?,
    val largeAssetText: String?,
    val smallAssetText: String?
) {
    companion object {
        fun from(timeRecord: TimeRecord): CurrentlyPlaying =
            CurrentlyPlaying(
                userId = timeRecord.userId,
                gameName = timeRecord.gameName,
                gameDetail = timeRecord.gameDetail,
                gameState = timeRecord.gameState,
                smallAssetText = timeRecord.smallAssetText,
                largeAssetText = timeRecord.largeAssetText,
                sessionBegin = timeRecord.sessionBegin
            )
    }
}
