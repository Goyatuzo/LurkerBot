package gameTime

import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import com.lurkerbot.discordUser.UserTracker
import com.lurkerbot.gameTime.GameTimeTracker
import com.lurkerbot.gameTime.GameTimer
import dev.kord.common.entity.ActivityType
import dev.kord.core.entity.Activity
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

    private val userA = mockkClass(User::class)
    private val userAEvent = mockkClass(PresenceUpdateEvent::class)

    private val userAId = (1234).toULong()
    private val guildAId = (4321).toULong()

    private fun mockActivity(type: ActivityType, gameName: String, gameDetails: String?): Activity {
        val mocked = mockkClass(Activity::class)

        every { mocked.type } returns type
        every { mocked.name } returns gameName
        every { mocked.details } returns gameDetails
        every { mocked.state } returns null
        every { mocked.assets } returns null

        return mocked
    }

    private val leagueOfLegendsActivity = mockActivity(ActivityType.Game, "League of Legends", null)
    private val leagueOfLegendsActivityTwo =
        mockActivity(ActivityType.Game, "League of Legends", "In Lobby")

    @Before
    fun setup() {
        every { userA.isBot } returns false
        every { userA.id.value } returns userAId

        every { userAEvent.guildId.value } returns guildAId
        coEvery { userAEvent.getUser() } returns userA

        every { userTracker.userIsBeingTracked(any()) } returns true
    }

    @After
    fun teardown() {
        clearMocks(userA, userAEvent)
    }

    @Test
    fun `A bot doesn't call anything`() = runBlocking {
        val botUser = mockkClass(User::class)
        val botUserEvent = mockkClass(PresenceUpdateEvent::class)

        every { botUser.isBot } returns true
        every { botUser.id.value } returns (1234).toULong()
        coEvery { botUserEvent.getUser() } returns botUser

        gameTimerTracker.processEvent(botUserEvent)

        verify {
            gameTimer wasNot called
            userTracker.userIsBeingTracked("1234")
        }

        confirmVerified(gameTimer, userTracker)
    }

    @Test
    fun `A user in a single server starts playing a game starts getting logged`() = runBlocking {
        every { userAEvent.presence.activities } returns listOf(leagueOfLegendsActivityTwo)
        every { userAEvent.old?.activities } returns listOf()
        every { gameTimer.beginLogging(userAId.toString(), guildAId.toString(), any()) } returns
            Ok(Unit)

        gameTimerTracker.processEvent(userAEvent)

        verify {
            gameTimer.beginLogging(
                userAId.toString(),
                guildAId.toString(),
                withArg {
                    assertThat(it.gameName).isEqualTo("League of Legends")
                    assertThat(it.gameDetail).isEqualTo("In Lobby")
                    assertThat(it.gameState).isNull()
                    assertThat(it.smallAssetText).isNull()
                    assertThat(it.largeAssetText).isNull()
                }
            )
            userTracker.userIsBeingTracked(userAId.toString())
        }

        confirmVerified(gameTimer, userTracker)
    }
}
