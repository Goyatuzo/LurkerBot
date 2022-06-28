package page

import discordUser.UserService
import gameTime.GameTimeService
import gameTime.TimerRepository
import java.time.LocalDateTime
import response.*

class PageService(
    private val userService: UserService,
    private val gameTimeService: GameTimeService,
    private val timerRepository: TimerRepository
) {
    fun getUserTimeStatsByDiscordId(userId: String, from: LocalDateTime): UserTimeStats? {
        val userInfo = userService.getUserByDiscordId(userId)
        val gameTimeStats = gameTimeService.getTimesForDiscordUserById(userId, from)
        val mostRecentStats =
            timerRepository.fiveMostRecentEntries(userId).map { RecentlyPlayed.from(it) }

        return if (userInfo == null) {
            null
        } else {
            UserTimeStats.of(userInfo, gameTimeStats, mostRecentStats)
        }
    }

    private fun groupTimeByProperty(
        stats: List<GameTimeDetailedSum>,
        keySelector: (GameTimeDetailedSum) -> String?
    ): List<TimeGraphData> =
        stats
            .groupBy(keySelector)
            .mapNotNull {
                if (it.key.isNullOrEmpty()) null
                else TimeGraphData.of(it.key!!, it.value.sumOf { t -> t.time })
            }
            .sortedByDescending(TimeGraphData::time)
            .take(6)

    fun getTimesForDiscordUserByIdAndGame(
        userId: String,
        gameName: String,
        from: LocalDateTime
    ): UserTimeStatsByGame? {
        val userInfo = userService.getUserByDiscordId(userId)
        val stats = timerRepository.getSummedGameTimeRecordsFor(userId, gameName, from)

        val groupedByDetail = groupTimeByProperty(stats, GameTimeDetailedSum::detail)
        val groupedByState = groupTimeByProperty(stats, GameTimeDetailedSum::state)
        val groupedBySmallAsset = groupTimeByProperty(stats, GameTimeDetailedSum::smallAsset)
        val groupedByLargeAsset = groupTimeByProperty(stats, GameTimeDetailedSum::largeAsset)

        return if (userInfo == null) {
            null
        } else {
            UserTimeStatsByGame.of(
                userInfo = userInfo,
                gameName = gameName,
                gameTime = stats,
                byGameDetail = groupedByDetail,
                byGameState = groupedByState,
                bySmallAsset = groupedBySmallAsset,
                byLargeAsset = groupedByLargeAsset
            )
        }
    }
}
