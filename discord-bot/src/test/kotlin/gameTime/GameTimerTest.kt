package gameTime

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import com.lurkerbot.gameTime.GameTimer
import currentlyPlaying.CurrentlyPlaying
import currentlyPlaying.CurrentlyPlayingService
import io.mockk.*
import java.time.LocalDateTime
import org.junit.After
import org.junit.Test

class GameTimerTest {
    private val serverId = "test server"

    private val timerRepository = mockk<TimerRepository>()
    private val currentlyPlayingService = mockk<CurrentlyPlayingService>()

    private val gameTimer = GameTimer(timerRepository, currentlyPlayingService)

    private val basicTimeRecord =
        TimeRecord(
            sessionBegin = LocalDateTime.now(),
            sessionEnd = LocalDateTime.now(),
            gameName = "game",
            userId = "test",
            gameDetail = "Detail",
            gameState = "State",
            largeAssetText = "",
            smallAssetText = ""
        )

    @After
    fun teardown() {
        clearMocks(timerRepository)
        clearMocks(currentlyPlayingService)
    }

    private fun setupCurrentlyPlayingService(currentlyPlaying: CurrentlyPlaying) {
        every {
            currentlyPlayingService.isUserCurrentlyPlayingById(currentlyPlaying.userId)
        } returns true
        every { currentlyPlayingService.getByUserId(currentlyPlaying.userId) } returns
            currentlyPlaying
        every { currentlyPlayingService.removeByUserId(currentlyPlaying.userId) } returns Unit
    }

    @Test
    fun `A person can start playing a game and an hour later, successfully ends logging`() {
        val later = LocalDateTime.now().plusHours(1)
        val toInsert = basicTimeRecord.copy()
        val toPlaying = CurrentlyPlaying.from(toInsert, serverId)
        setupCurrentlyPlayingService(toPlaying)
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", serverId, toInsert)
        val actual = gameTimer.endLogging("test", serverId, later)
        assertThat(actual).isEqualTo(Ok(Unit))

        verify(exactly = 1) { timerRepository.saveTimeRecord(toInsert.copy(sessionEnd = later)) }

        confirmVerified(timerRepository)
    }

    @Test
    fun `When duplicate user and game beings logging, error is returned`() {
        val toInsert = basicTimeRecord.copy()
        val secondGame = basicTimeRecord.copy(gameState = "Updated State")
        val toPlaying = CurrentlyPlaying.from(toInsert, serverId)
        setupCurrentlyPlayingService(toPlaying)
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", serverId, toInsert)
        val error = gameTimer.beginLogging("test", serverId, secondGame)

        assertThat(error).isEqualTo(Err(GameIsAlreadyLogging("test", secondGame)))
    }

    @Test
    fun `When two servers start logging for same user, only the first is added`() {
        val firstToInsert = basicTimeRecord.copy()

        val fakeTime = LocalDateTime.of(2020, 1, 1, 1, 0, 0)
        val secondToInsert = basicTimeRecord.copy(sessionBegin = fakeTime)

        val toPlayingFirst = CurrentlyPlaying.from(firstToInsert, serverId)
        val toPlayingSecond = CurrentlyPlaying.from(secondToInsert, serverId)
        setupCurrentlyPlayingService(toPlayingFirst)
        setupCurrentlyPlayingService(toPlayingSecond)
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", serverId, firstToInsert)
        val error = gameTimer.beginLogging("test", "test server 2", secondToInsert)

        assertThat(error).isEqualTo(Err(GameIsAlreadyLogging("test", secondToInsert)))
    }

    @Test
    fun `When two servers start logging for same user and ends, only the first is saved to database`() {
        val firstToInsert = basicTimeRecord.copy()
        val later = LocalDateTime.now().plusHours(1)

        val fakeTime = LocalDateTime.of(2020, 1, 1, 1, 0, 0)
        val secondToInsert = basicTimeRecord.copy(sessionBegin = fakeTime)

        val toPlayingFirst = CurrentlyPlaying.from(firstToInsert, serverId)
        val toPlayingSecond = CurrentlyPlaying.from(secondToInsert, serverId)
        setupCurrentlyPlayingService(toPlayingFirst)
        setupCurrentlyPlayingService(toPlayingSecond)
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", serverId, firstToInsert)
        gameTimer.beginLogging("test", "test server 2", secondToInsert)

        gameTimer.endLogging("test", serverId, later)
        gameTimer.endLogging("test", "test server 2", later)

        verify(exactly = 1) {
            timerRepository.saveTimeRecord(record = not(firstToInsert.copy(sessionEnd = later)))
            timerRepository.saveTimeRecord(record = secondToInsert.copy(sessionEnd = later))
        }

        confirmVerified(timerRepository)
    }

    @Test
    fun `Should not be be able to save logging that never started`() {
        every { timerRepository.saveTimeRecord(any()) } returns Unit
        every { currentlyPlayingService.getByUserId(any()) } returns null

        val actual =
            gameTimer.endLogging(
                "test",
                serverId,
            )
        assertThat(actual)
            .isEqualTo(
                Err(
                    NeverStartedLogging(
                        "test",
                        serverId,
                    )
                )
            )

        verify { timerRepository wasNot Called }

        confirmVerified(timerRepository)
    }

    @Test
    fun `When logging ends, a new one should be able to start`() {
        val now = LocalDateTime.now().plusHours(1)
        val toInsert = basicTimeRecord.copy()

        val toPlaying = CurrentlyPlaying.from(toInsert, serverId)

        every { currentlyPlayingService.isUserCurrentlyPlayingById(toPlaying.userId) } returns
            true andThen
            false
        every { currentlyPlayingService.getByUserId(toPlaying.userId) } returns toPlaying
        every { currentlyPlayingService.removeByUserId(toPlaying.userId) } returns Unit
        every { currentlyPlayingService.save(any()) } returns Unit
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", serverId, toInsert)
        gameTimer.endLogging("test", serverId, now)
        val begin = gameTimer.beginLogging("test", serverId, toInsert)
        assertThat(begin).isEqualTo(Ok(Unit))
    }

    @Test
    fun `Playing a game for milliseconds is not valid`() {
        val now = LocalDateTime.now()
        val toInsert = basicTimeRecord.copy()

        val toPlaying = CurrentlyPlaying.from(toInsert, serverId)
        setupCurrentlyPlayingService(toPlaying)
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", serverId, toInsert)
        val actual = gameTimer.endLogging("test", serverId, now)
        assertThat(actual)
            .isEqualTo(Err(StateChangedTooFast("test", toInsert.copy(sessionEnd = now))))

        verify { timerRepository wasNot called }

        confirmVerified(timerRepository)
    }
}
