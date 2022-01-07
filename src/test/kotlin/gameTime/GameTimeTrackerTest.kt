package gameTime

import com.lurkerbot.discordUser.UserTracker
import com.lurkerbot.gameTime.GameTimeTracker
import com.lurkerbot.gameTime.GameTimer
import dev.kord.core.entity.User
import dev.kord.core.event.user.PresenceUpdateEvent
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GameTimeTrackerTest {
    private val gameTimer = mockk<GameTimer>()
    private val userTracker = mockk<UserTracker>()

    private val gameTimerTracker = GameTimeTracker(gameTimer, userTracker)

//    private val userA = mockkClass(User::class)

    @Before fun setup() {}

    @After fun teardown() {}

    @Test
    fun `A bot doesn't call anything`() = runBlocking {
        val botUser = mockkClass(User::class)
        val botUserEvent = mockkClass(PresenceUpdateEvent::class)

        every { botUser.isBot } returns true
        every { botUser.id.value } returns (1234).toULong()
        coEvery { botUserEvent.getUser() } returns botUser

        every { userTracker.userIsBeingTracked(any()) } returns true

        gameTimerTracker.processEvent(botUserEvent)

        verify {
            gameTimer wasNot called
            userTracker.userIsBeingTracked("1234")
        }

        confirmVerified(gameTimer, userTracker)
    }
}
