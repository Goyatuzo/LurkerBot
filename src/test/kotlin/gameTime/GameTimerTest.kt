package gameTime

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Test
import java.time.LocalDate

class GameTimerTest {
    private val timerRepository = mockk<TimerRepository>()

    private val gameTimer = GameTimer(timerRepository)

    private val basicTimeRecord = TimeRecord(
        sessionBegin = LocalDate.now(),
        sessionEnd = LocalDate.MAX,
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
        assertThat(actual).isEqualTo(toInsert)

        verify {
            timerRepository.saveTimeRecord(toInsert)
        }

        confirmVerified(timerRepository)
    }

    @Test
    fun `Should not be be able to end logging that never started`() {
        every { timerRepository.saveTimeRecord(any()) } returns Unit

        val actual = gameTimer.endLogging("test", "game")
        assertThat(actual).isNull()

        verify {
            timerRepository wasNot Called
        }

        confirmVerified(timerRepository)
    }
}