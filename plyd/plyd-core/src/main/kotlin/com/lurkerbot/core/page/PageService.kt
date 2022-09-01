package com.lurkerbot.core.page

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.binding
import com.lurkerbot.core.currentlyPlaying.CurrentlyPlayingService
import com.lurkerbot.core.discordUser.UserService
import com.lurkerbot.core.error.DomainError
import com.lurkerbot.core.gameTime.GameTimeService
import com.lurkerbot.core.gameTime.TimeSummaryService
import com.lurkerbot.core.gameTime.TimerRepository
import com.lurkerbot.core.response.*
import java.time.LocalDateTime

class PageService(
    private val userService: UserService,
    private val gameTimeService: GameTimeService,
    private val timerRepository: TimerRepository,
    private val timeSummaryService: TimeSummaryService,
    private val currentlyPlayingService: CurrentlyPlayingService
) {
    fun getUserTimeStatsByDiscordId(
        userId: String,
        from: LocalDateTime
    ): Result<UserTimeStats, DomainError> = binding {
        val userInfo = userService.getUserByDiscordId(userId).bind()
        val gameTimeStats = gameTimeService.getTimesForDiscordUserById(userId, from)
        val mostRecentStats =
            timerRepository.mostRecentEntries(userId, 8).map { RecentlyPlayed.from(it) }
        val currentlyPlaying = currentlyPlayingService.getByUserId(userId).bind()

        UserTimeStats.of(userInfo, gameTimeStats, mostRecentStats, currentlyPlaying)
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

    fun getTimesForDiscordUserByIdAndGame(
        userId: String,
        gameName: String,
        from: LocalDateTime
    ): Result<UserTimeStatsByGame, DomainError> =
        userService.getUserByDiscordId(userId).andThen { userInfo ->
            val stats = timerRepository.getSummedGameTimeRecordsFor(userId, gameName, from)

            val groupedByDetail = groupTimeByProperty(stats, GameTimeDetailedSum::detail)
            val groupedByState = groupTimeByProperty(stats, GameTimeDetailedSum::state)
            val groupedBySmallAsset = groupTimeByProperty(stats, GameTimeDetailedSum::smallAsset)
            val groupedByLargeAsset = groupTimeByProperty(stats, GameTimeDetailedSum::largeAsset)

            Ok(
                UserTimeStatsByGame.of(
                    userInfo = userInfo,
                    gameName = gameName,
                    gameTime = stats,
                    byGameDetail = groupedByDetail,
                    byGameState = groupedByState,
                    bySmallAsset = groupedBySmallAsset,
                    byLargeAsset = groupedByLargeAsset
                )
            )
        }

    fun twoWeekSiteStats(): List<GameTimeSum> = timeSummaryService.getAllTimesFromPastWeek()
}
