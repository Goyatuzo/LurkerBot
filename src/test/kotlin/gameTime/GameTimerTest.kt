package gameTime

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Test
import java.time.LocalTime

class GameTimerTest {
    private val timerRepository = mockk<TimerRepository>()

    private val gameTimer = GameTimer(timerRepository)

    private val basicTimeRecord = TimeRecord(
        sessionBegin = LocalTime.now(),
        sessionEnd = LocalTime.MAX,
        gameName = "game",
        userId = "test",
        gameDetail = "Detail",
        gameState = "State",
        largeAssetText = "",
        smallAssetText = "",
        gameType = ""
    )

    @Test
    fun `Should be able to start logging a brand new game and end it`() {
        val toInsert = basicTimeRecord.copy()
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "game", toInsert)
        val actual = gameTimer.endLogging("test", "game")
        assertThat(actual).isEqualTo(Ok(toInsert))

        verify {
            timerRepository.saveTimeRecord(toInsert)
        }

        confirmVerified(timerRepository)
    }

    @Test
    fun `When duplicate user and game beings logging, error is retuend`() {
        val toInsert = basicTimeRecord.copy()
        val secondGame = basicTimeRecord.copy(gameState = "Updated State")
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "game", toInsert)
        val error = gameTimer.beginLogging("test", "game", secondGame)

        assertThat(error).isEqualTo(Err(GameIsAlreadyLogging("test", "game", toInsert, secondGame)))
    }

    @Test
    fun `Should not be be able to end logging that never started`() {
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        val actual = gameTimer.endLogging("test", "game")
        assertThat(actual).isEqualTo(Err(NeverStartedLogging("test", "game")))

        verify {
            timerRepository wasNot Called
        }

        confirmVerified(timerRepository)
    }
}