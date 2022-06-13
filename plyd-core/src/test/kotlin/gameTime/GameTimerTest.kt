package gameTime

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import java.time.LocalDateTime
import org.junit.After
import org.junit.Test

class GameTimerTest {
    private val timerRepository = mockk<TimerRepository>()

    private val gameTimer = GameTimer(timerRepository)

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
    }

    @Test
    fun `A person can start playing a game and an hour later, successfully ends logging`() {
        val later = LocalDateTime.now().plusHours(1)
        val toInsert = basicTimeRecord.copy()
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "test server", toInsert)
        val actual = gameTimer.endLogging("test", "test server", later)
        assertThat(actual).isEqualTo(Ok(Unit))

        verify { timerRepository.saveTimeRecord(toInsert.copy(sessionEnd = later)) }

        confirmVerified(timerRepository)
    }

    @Test
    fun `When duplicate user and game beings logging, error is returned`() {
        val toInsert = basicTimeRecord.copy()
        val secondGame = basicTimeRecord.copy(gameState = "Updated State")
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "test server", toInsert)
        val error = gameTimer.beginLogging("test", "test server", secondGame)

        assertThat(error).isEqualTo(Err(GameIsAlreadyLogging("test", toInsert, secondGame)))
    }

    @Test
    fun `When two servers start logging for same user, only the first is added`() {
        val firstToInsert = basicTimeRecord.copy()

        val fakeTime = LocalDateTime.of(2020, 1, 1, 1, 0, 0)
        val secondToInsert = basicTimeRecord.copy(sessionBegin = fakeTime)
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "test server", firstToInsert)
        val error = gameTimer.beginLogging("test", "test server 2", secondToInsert)

        assertThat(error)
            .isEqualTo(Err(GameIsAlreadyLogging("test", firstToInsert, secondToInsert)))
    }

    @Test
    fun `When two servers start logging for same user and ends, only the first is saved to database`() {
        val firstToInsert = basicTimeRecord.copy()
        val later = LocalDateTime.now().plusHours(1)

        val fakeTime = LocalDateTime.of(2020, 1, 1, 1, 0, 0)
        val secondToInsert = basicTimeRecord.copy(sessionBegin = fakeTime)
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "test server", firstToInsert)
        gameTimer.beginLogging("test", "test server 2", secondToInsert)

        gameTimer.endLogging("test", "test server", later)
        gameTimer.endLogging("test", "test server 2", later)

        verify(exactly = 1) {
            timerRepository.saveTimeRecord(record = firstToInsert.copy(sessionEnd = later))
            timerRepository.saveTimeRecord(record = not(secondToInsert.copy(sessionEnd = later)))
        }

        confirmVerified(timerRepository)
    }

    @Test
    fun `Should not be be able to save logging that never started`() {
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        val actual =
            gameTimer.endLogging(
                "test",
                "test server",
            )
        assertThat(actual)
            .isEqualTo(
                Err(
                    NeverStartedLogging(
                        "test",
                        "test server",
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
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "test server", toInsert)
        gameTimer.endLogging("test", "test server", now)
        val begin = gameTimer.beginLogging("test", "test server", toInsert)
        assertThat(begin).isEqualTo(Ok(Unit))
    }

    @Test
    fun `Playing a game for milliseconds is not valid`() {
        val now = LocalDateTime.now()
        val toInsert = basicTimeRecord.copy()
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "test server", toInsert)
        val actual = gameTimer.endLogging("test", "test server", now)
        assertThat(actual)
            .isEqualTo(Err(StateChangedTooFast("test", toInsert.copy(sessionEnd = now))))

        verify { timerRepository wasNot called }

        confirmVerified(timerRepository)
    }
}
