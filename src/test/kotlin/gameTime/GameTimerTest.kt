package gameTime

import com.google.common.truth.Truth.assertThat
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.time.LocalDate

class GameTimerTest {
    private val timerRepository = mockk<TimerRepository>()

    val gameTimer = GameTimer(timerRepository)

    @Test
    fun `Should be able to start logging a brand new game and end it`() {
        val toInsert = TimeRecord(
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

        every { timerRepository.saveTimeRecord(any()) } returns Unit

        gameTimer.beginLogging("test", "game", toInsert)
        val actual = gameTimer.endLogging("test", "game")
        assertThat(actual).isEqualTo(toInsert)

        verify {
            timerRepository.saveTimeRecord(toInsert)
        }

        confirmVerified(timerRepository)
    }
}