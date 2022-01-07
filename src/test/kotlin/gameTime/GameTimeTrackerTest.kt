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
    private val userAPostEvent = mockkClass(PresenceUpdateEvent::class)

    private val userAId = (1234).toULong()
    private val guildAId = (4321).toULong()

    private fun mockActivity(
        type: ActivityType,
        gameName: String,
        gameDetails: String? = null,
        gameState: String? = null,
        smallTextAsset: String? = null,
        largeTextAsset: String? = null
    ): Activity {
        val mocked = mockkClass(Activity::class)
        val mockedAssets = mockkClass(Activity.Assets::class)

        every { mockedAssets.smallText } returns smallTextAsset
        every { mockedAssets.largeText } returns largeTextAsset

        every { mocked.type } returns type
        every { mocked.name } returns gameName
        every { mocked.details } returns gameDetails
        every { mocked.state } returns gameState
        every { mocked.assets } returns mockedAssets

        return mocked
    }

    private val leagueOfLegendsActivity =
        mockActivity(ActivityType.Game, "League of Legends", "SR", "In Game", "Caitlyn", "lv18")
    private val leagueOfLegendsActivityTwo =
        mockActivity(ActivityType.Game, "League of Legends", "In Lobby")

    @Before
    fun setup() {
        every { userA.isBot } returns false
        every { userA.id.value } returns userAId

        every { userAEvent.guildId.value } returns guildAId
        coEvery { userAEvent.getUser() } returns userA

        every { userAPostEvent.guildId.value } returns guildAId
        coEvery { userAPostEvent.getUser() } returns userA

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
        every { userAEvent.presence.activities } returns listOf(leagueOfLegendsActivity)
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
                    assertThat(it.gameDetail).isEqualTo("SR")
                    assertThat(it.gameState).isEqualTo("In Game")
                    assertThat(it.smallAssetText).isEqualTo("Caitlyn")
                    assertThat(it.largeAssetText).isEqualTo("lv18")
                }
            )
            userTracker.userIsBeingTracked(userAId.toString())
            gameTimer.endLogging(any(), any(), any()) wasNot called
        }

        confirmVerified(gameTimer, userTracker)
    }

    @Test
    fun `A user in a single server plays 1 game that and its Presence updates`() = runBlocking {
        every { userAEvent.presence.activities } returns listOf(leagueOfLegendsActivity)
        every { userAEvent.old?.activities } returns listOf()
        every { userAPostEvent.presence.activities } returns listOf(leagueOfLegendsActivityTwo)
        every { userAPostEvent.old?.activities } returns listOf(leagueOfLegendsActivity)
        every { gameTimer.beginLogging(userAId.toString(), guildAId.toString(), any()) } returns
            Ok(Unit)
        every { gameTimer.endLogging(userAId.toString(), guildAId.toString(), any()) } returns
            Ok(Unit)

        // User starts playing a game
        gameTimerTracker.processEvent(userAEvent)
        // The game's presence changes
        gameTimerTracker.processEvent(userAPostEvent)

        verify { userTracker.userIsBeingTracked(userAId.toString()) }

        verifyOrder {
            gameTimer.beginLogging(
                userAId.toString(),
                guildAId.toString(),
                withArg {
                    assertThat(it.gameName).isEqualTo("League of Legends")
                    assertThat(it.gameDetail).isEqualTo("SR")
                    assertThat(it.gameState).isEqualTo("In Game")
                    assertThat(it.smallAssetText).isEqualTo("Caitlyn")
                    assertThat(it.largeAssetText).isEqualTo("lv18")
                }
            )
            gameTimer.endLogging(userAId.toString(), guildAId.toString(), any())
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
        }

        confirmVerified(gameTimer, userTracker)
    }
}
