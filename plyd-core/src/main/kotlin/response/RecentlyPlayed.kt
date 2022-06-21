package response

import gameTime.TimeRecord
import java.time.Duration
import java.time.LocalDateTime

@kotlinx.serialization.Serializable
data class RecentlyPlayed(
    val gameName: String,
    val gameDetail: String?,
    val gameState: String?,
    val smallAssetText: String?,
    val largeAssetText: String?,
    val timePlayed: Double,
    val playedAgo: String
) {
    companion object {
        fun from(timeRecord: TimeRecord): RecentlyPlayed {
            val timePlayed =
                Duration.between(timeRecord.sessionBegin, timeRecord.sessionEnd)
                    .toMinutes()
                    .toDouble()
            val timePlayedAgo =
                Duration.between(timeRecord.sessionEnd, LocalDateTime.now()).toSeconds()

            return RecentlyPlayed(
                gameName = timeRecord.gameName,
                gameDetail = timeRecord.gameDetail,
                gameState = timeRecord.gameState,
                smallAssetText = timeRecord.smallAssetText,
                largeAssetText = timeRecord.largeAssetText,
                timePlayed = timePlayed / 60,
                playedAgo =
                    when {
                        timePlayedAgo < 60 -> "$timePlayedAgo second(s)"
                        timePlayedAgo < 3600 -> "${timePlayedAgo / 60} minute(s)"
                        else -> "${timePlayedAgo / 3600} hour(s)"
                    }
            )
        }
    }
}
