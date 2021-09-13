package rchabot.services.tournament.impl

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.failureOrNull
import dev.forkhandles.result4k.get
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import rchabot.common.exception.NotFoundException
import rchabot.model.Player
import rchabot.model.Tournament
import rchabot.repository.tournament.TournamentRepository
import rchabot.services.player.bo.PlayerBO
import rchabot.services.player.mapper.PlayerMapper
import rchabot.services.tournament.bo.TournamentBO
import rchabot.services.tournament.mapper.TournamentMapper
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class TournamentServiceImplTest {

    @MockK
    private lateinit var tournamentMapper: TournamentMapper

    @MockK
    private lateinit var tournamentRepository: TournamentRepository

    @MockK
    private lateinit var playerMapper: PlayerMapper

    private lateinit var service: TournamentServiceImpl

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        service = spyk(TournamentServiceImpl(tournamentMapper, tournamentRepository, playerMapper))
    }

    @Nested
    inner class Create {

        @Test
        fun `should return failure with message when tournament with name already exists`() = runBlocking {

            // Arrange
            val name = "data"

            // Mock
            coEvery { tournamentRepository.existsByName(name) } returns true

            // Act
            val result = service.create(name)

            // Assert
            assertTrue { result is Failure }
            assertEquals("Tournament with name $name already exists", result.failureOrNull()!!.message)

        }

        @Test
        fun `should return succes when tournament with name doesn't already exists`() = runBlocking {

            // Arrange
            val name = "data"
            val createdTournament = Tournament(_id = ObjectId(), name = name)
            val createdTournamentBO = TournamentBO(name = name)

            // Mock
            coEvery { tournamentRepository.existsByName(name) } returns false
            coEvery { tournamentRepository.create(Tournament(name = name)) } returns createdTournament
            coEvery { tournamentMapper.toBO(createdTournament) } returns createdTournamentBO

            // Act
            val result = service.create(name)

            // Assert
            assertTrue { result is Success }
            assertSame(createdTournamentBO, result.get())

        }
    }

    @Nested
    inner class FindById {

        @Test
        fun `should find tournament by id`() = runBlocking {

            // Arrange
            val tournamentId = ObjectId()
            val tournament = Tournament(name = "")
            val tournamentBO = TournamentBO(name = "")

            // Mock
            coEvery { tournamentRepository.findById(tournamentId) } returns tournament
            coEvery { tournamentMapper.toBO(tournament) } returns tournamentBO

            // Act
            val result = service.findById(tournamentId)

            // Assert
            assertSame(tournamentBO, result)
        }
    }

    @Nested
    inner class Delete {

        @Test
        fun `should delete tournament`() = runBlocking {
            // Arrange
            val tournamentId = ObjectId()

            // Mock
            coJustRun { tournamentRepository.delete(tournamentId) }

            // Act
            val result = service.delete(tournamentId)

            // Assert
            coVerify { tournamentRepository.delete(tournamentId) }
        }

    }

    @Nested
    inner class RegisterPlayer {

        @Test
        fun `should return failure with message when player already registered`() = runBlocking {

            // Arrange
            val playerName = "playerName"
            val tournamentId = ObjectId()
            val tournamentBO = mockk<TournamentBO>()
            val playerBO = PlayerBO(playerName)

            // Mock
            coEvery { service.findById(tournamentId) } returns tournamentBO
            every { tournamentBO.existsPlayer(playerName) } returns true

            // Act
            val result = service.registerPlayer(tournamentId, playerBO)

            // Assert
            assertTrue { result is Failure }
            assertEquals("Player $playerName is already registered", result.failureOrNull()!!.message)

        }

        @Test
        fun `should return succes with added player when player not registerd`() = runBlocking {

            // Arrange
            val playerName = "playerName"
            val tournamentId = ObjectId()
            val tournamentBO = mockk<TournamentBO>()
            val updatedTournamentBO = mockk<TournamentBO>()
            val player = Player(playerName)
            val playerBO = PlayerBO(playerName)

            // Mock
            coEvery { service.findById(tournamentId) } returns tournamentBO
            every { tournamentBO.existsPlayer(playerName) } returns false
            every { playerMapper.toModel(playerBO) } returns player
            coJustRun { tournamentRepository.registerPlayer(tournamentId, player) }
            coEvery { service.updatePlayerScore(tournamentId, playerBO) } returns updatedTournamentBO

            // Act
            val result = service.registerPlayer(tournamentId, playerBO)

            // Assert
            coVerify { tournamentRepository.registerPlayer(tournamentId, player) }
            assertTrue { result is Success }
            assertSame(updatedTournamentBO, result.get())

        }

    }

    @Nested
    inner class UpdatePlayerScore {


        @ParameterizedTest
        @MethodSource("provideUpdatePlayerScoreTestData")
        fun `should update player score and update all players ranking`(testData: UpdateScoreTestData) = runBlocking {

            // Arrange
            val tournamentId = ObjectId()
            val tournamentBO =
                TournamentBO(_id = tournamentId, name = "", players = testData.initialLeaderboard)

            val savedTournamentCaptor = slot<TournamentBO>()
            val tournamentToSave = Tournament(name = "")
            val savedTournament = Tournament(name = "")
            val savedTournamentBO = TournamentBO(name = "")

            // Mock
            coEvery { service.findById(tournamentId) } returns tournamentBO
            every { tournamentMapper.toModel(capture(savedTournamentCaptor)) } returns tournamentToSave
            coEvery { tournamentRepository.update(tournamentToSave) } returns savedTournament
            every { tournamentMapper.toBO(savedTournament) } returns savedTournamentBO

            // Act
            val result = service.updatePlayerScore(tournamentId, testData.updatePlayer)

            // Assert
            assertSame(savedTournamentBO, result)
            assertEquals(testData.expectedLeaderboard, savedTournamentCaptor.captured.players)

        }

        fun provideUpdatePlayerScoreTestData(): Stream<Arguments> = Stream.of(
            Arguments.of(
                UpdateScoreTestData(
                    listOf(
                        PlayerBO("player1", score = 1, ranking = 1),
                        PlayerBO("player2", score = 2, ranking = 2),
                        PlayerBO("player3", score = 3, ranking = 3),
                        PlayerBO("player4", score = 4, ranking = 4),
                        PlayerBO("player5", score = 5, ranking = 5),
                        PlayerBO("player6", score = 6, ranking = 6),
                    ),
                    PlayerBO("player4", score = 10, ranking = null),
                    listOf(
                        PlayerBO("player4", score = 10, ranking = 1),
                        PlayerBO("player6", score = 6, ranking = 2),
                        PlayerBO("player5", score = 5, ranking = 3),
                        PlayerBO("player3", score = 3, ranking = 4),
                        PlayerBO("player2", score = 2, ranking = 5),
                        PlayerBO("player1", score = 1, ranking = 6),
                    )
                )
            ), Arguments.of(
                UpdateScoreTestData(
                    listOf(
                        PlayerBO("player1", score = 1, ranking = 0),
                        PlayerBO("player2", score = 3, ranking = 0),
                        PlayerBO("player3", score = 4, ranking = 0),
                    ),
                    PlayerBO("player3", score = 2, ranking = 0),
                    listOf(
                        PlayerBO("player2", score = 3, ranking = 1),
                        PlayerBO("player3", score = 2, ranking = 2),
                        PlayerBO("player1", score = 1, ranking = 3),
                    )
                )
            ),
            Arguments.of(
                UpdateScoreTestData(
                    listOf(
                        PlayerBO("player1", score = 1, ranking = 0),
                        PlayerBO("player2", score = 3, ranking = 0),
                        PlayerBO("player3", score = 4, ranking = 0),
                    ),
                    PlayerBO("player2", score = -2, ranking = 0),
                    listOf(
                        PlayerBO("player3", score = 4, ranking = 1),
                        PlayerBO("player1", score = 1, ranking = 2),
                        PlayerBO("player2", score = -2, ranking = 3),
                    )
                )
            )

        )

        @ParameterizedTest
        @MethodSource("provideUpdatePlayerScoreSameRankingTestData")
        fun `should update player score and update all players ranking with same ranking when players have same score`(
            testData: UpdateScoreTestData
        ) = runBlocking {

            // Arrange
            val tournamentId = ObjectId()
            val tournamentBO =
                TournamentBO(_id = tournamentId, name = "", players = testData.initialLeaderboard)

            val savedTournamentCaptor = slot<TournamentBO>()
            val tournamentToSave = Tournament(name = "")
            val savedTournament = Tournament(name = "")
            val savedTournamentBO = TournamentBO(name = "")

            // Mock
            coEvery { service.findById(tournamentId) } returns tournamentBO
            every { tournamentMapper.toModel(capture(savedTournamentCaptor)) } returns tournamentToSave
            coEvery { tournamentRepository.update(tournamentToSave) } returns savedTournament
            every { tournamentMapper.toBO(savedTournament) } returns savedTournamentBO

            // Act
            val result = service.updatePlayerScore(tournamentId, testData.updatePlayer)

            // Assert
            assertSame(savedTournamentBO, result)
            assertEquals(testData.expectedLeaderboard.toSet(), savedTournamentCaptor.captured.players.toSet())

        }

        fun provideUpdatePlayerScoreSameRankingTestData(): Stream<Arguments> = Stream.of(
            Arguments.of(
                UpdateScoreTestData(
                    listOf(
                        PlayerBO("player1", score = 1, ranking = 1),
                        PlayerBO("player2", score = 2, ranking = 2),
                        PlayerBO("player3", score = 3, ranking = 3),
                        PlayerBO("player4", score = 4, ranking = 4),
                        PlayerBO("player5", score = 5, ranking = 5),
                        PlayerBO("player6", score = 6, ranking = 6),
                    ),
                    PlayerBO("player4", score = 2, ranking = null),
                    listOf(
                        PlayerBO("player6", score = 6, ranking = 1),
                        PlayerBO("player5", score = 5, ranking = 2),
                        PlayerBO("player3", score = 3, ranking = 3),
                        PlayerBO("player2", score = 2, ranking = 4),
                        PlayerBO("player4", score = 2, ranking = 4),
                        PlayerBO("player1", score = 1, ranking = 5),
                    )
                )
            ), Arguments.of(
                UpdateScoreTestData(
                    listOf(
                        PlayerBO("player1", score = 0, ranking = 0),
                        PlayerBO("player2", score = 0, ranking = 0),
                        PlayerBO("player3", score = 0, ranking = 0),
                    ),
                    PlayerBO("player3", score = 0, ranking = 0),
                    listOf(
                        PlayerBO("player2", score = 0, ranking = 1),
                        PlayerBO("player3", score = 0, ranking = 1),
                        PlayerBO("player1", score = 0, ranking = 1),
                    )
                )
            ),
            Arguments.of(
                UpdateScoreTestData(
                    listOf(
                        PlayerBO("player1", score = 1, ranking = 0),
                        PlayerBO("player2", score = 3, ranking = 0),
                        PlayerBO("player3", score = 4, ranking = 0),
                    ),
                    PlayerBO("player2", score = 4, ranking = 0),
                    listOf(
                        PlayerBO("player3", score = 4, ranking = 1),
                        PlayerBO("player2", score = 4, ranking = 1),
                        PlayerBO("player1", score = 1, ranking = 2),
                    )
                )
            )

        )

        @Test
        fun `should throw NotFoundException when player is not registered in tournament`() = runBlocking {

            // Arrange
            val tournamentId = ObjectId()
            val tournamentBO = mockk<TournamentBO>()
            val playerName = "player1"

            // Mock
            coEvery { service.findById(tournamentId) } returns tournamentBO
            every { tournamentBO.findPlayer(playerName) } returns null

            // Act
            val exception = assertFailsWith<NotFoundException> {
                service.updatePlayerScore(tournamentId, PlayerBO(playerName))
            }

            // Assert
            assertEquals("Player player1 is not registered to tournament $tournamentId", exception.message)
            coVerify { tournamentRepository.update(any()) wasNot called }
            verify { tournamentMapper.toModel(any()) wasNot called }
            verify { tournamentMapper.toBO(any()) wasNot called }

        }

    }

    data class UpdateScoreTestData(
        val initialLeaderboard: List<PlayerBO>,
        val updatePlayer: PlayerBO,
        val expectedLeaderboard: List<PlayerBO>
    )

    @Nested
    inner class FindPlayer {

        @Test
        internal fun `should throw NotFoundException when player not registered in tournament`() = runBlocking {
            // Arrange
            val tournamentId = ObjectId()
            val tournamentBO = mockk<TournamentBO>()
            val playerName = "playerName"

            // Mock
            coEvery { service.findById(tournamentId) } returns tournamentBO
            every { tournamentBO.findPlayer(playerName) } returns null

            // Act
            val exception = assertFailsWith<NotFoundException> {
                service.findPlayer(tournamentId, playerName)
            }

            // Assert
            assertEquals("Player $playerName is not registered to tournament $tournamentId", exception.message)

        }

        @Test
        internal fun `should return the player by playerName when player registered in tournament`() = runBlocking {

            // Arrange
            val tournamentId = ObjectId()
            val tournamentBO = mockk<TournamentBO>()
            val playerName = "playerName"
            val playerBO = PlayerBO("")

            // Mock
            coEvery { service.findById(tournamentId) } returns tournamentBO
            every { tournamentBO.findPlayer(playerName) } returns playerBO

            // Act
            val result = service.findPlayer(tournamentId, playerName)

            // Assert
            assertSame(playerBO, result)
        }
    }

    @Nested
    inner class DeletePlayers {

        @Test
        internal fun `should delete all tournament players`() = runBlocking {
            // Arrange
            val tournamentId = ObjectId()
            val tournamentBO = TournamentBO(
                tournamentId,
                "test",
                listOf<PlayerBO>(PlayerBO(playerName = "player1"), PlayerBO(playerName = "player2"))
            )

            val tournamentToSave = Tournament("a")
            val savedTournament = Tournament("b")
            val savedTournamentBO = TournamentBO("")

            // Mock
            coEvery { service.findById(tournamentId) } returns tournamentBO
            every { tournamentMapper.toModel(tournamentBO) } returns tournamentToSave
            coEvery { tournamentRepository.update(tournamentToSave) } returns savedTournament
            every { tournamentMapper.toBO(savedTournament) } returns savedTournamentBO

            // Act
            val result = service.deletePlayers(tournamentId)

            // Assert
            assertSame(savedTournamentBO, result)
            assertTrue { tournamentBO.players.isEmpty() }

        }
    }

}
