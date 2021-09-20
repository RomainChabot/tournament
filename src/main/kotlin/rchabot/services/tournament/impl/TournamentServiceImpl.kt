package rchabot.services.tournament.impl

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.map
import org.bson.types.ObjectId
import rchabot.common.exception.NotFoundException
import rchabot.dao.tournament.TournamentRepository
import rchabot.model.Tournament
import rchabot.services.common.mapper.mapAround
import rchabot.services.common.mapper.mapBO
import rchabot.services.player.bo.PlayerBO
import rchabot.services.player.mapper.PlayerMapper
import rchabot.services.tournament.TournamentService
import rchabot.services.tournament.bo.TournamentBO
import rchabot.services.tournament.mapper.TournamentMapper

class TournamentServiceImpl(
    private val tournamentMapper: TournamentMapper,
    private val tournamentRepository: TournamentRepository,
    private val playerMapper: PlayerMapper,
) : TournamentService {

    override suspend fun findAll(): Collection<TournamentBO> {
        return tournamentRepository.findAll().map(tournamentMapper::toBO)
    }

    override suspend fun create(name: String): Result4k<TournamentBO, Error> {
        if (tournamentRepository.existsByName(name)) {
            return Failure(Error("Tournament with name $name already exists"))
        }
        return Success(tournamentRepository.create(Tournament(name = name)))
            .map(tournamentMapper::toBO)
    }

    override suspend fun findById(tournamentId: ObjectId): TournamentBO {
        return tournamentMapper mapBO {
            tournamentRepository.findById(tournamentId)
        }
    }

    override suspend fun delete(tournamentId: ObjectId) {
        tournamentRepository.delete(tournamentId)
    }

    override suspend fun getLeaderboard(tournamentId: ObjectId): List<PlayerBO> {
        return findById(tournamentId).players
    }

    override suspend fun registerPlayer(tournamentId: ObjectId, playerName: String): Result4k<List<PlayerBO>, Error> {
        val tournament = this.findById(tournamentId)
        if (tournament.existsPlayer(playerName)) {
            return Failure(Error("Player $playerName is already registered"))
        }
        val player = PlayerBO(playerName = playerName, score = 0, ranking = null)
        tournamentRepository.registerPlayer(tournamentId, playerMapper.toModel(player))
        return Success(this.updatePlayerScore(tournamentId, player))
    }

    override suspend fun updatePlayerScore(tournamentId: ObjectId, player: PlayerBO): List<PlayerBO> {
        val tournament = this.findById(tournamentId)
        tournament.findPlayer(player.playerName)?.let {
            it.score = player.score
            updatePlayersRanking(tournament)
            return tournamentMapper.mapAround(tournamentRepository::update, tournament).players
        } ?: throw NotFoundException("Player ${player.playerName} is not registered to tournament $tournamentId")
    }

    private fun updatePlayersRanking(tournament: TournamentBO) {
        val leaderboard = tournament.players.sortedBy(PlayerBO::score)
        val playersByScore = tournament.players.groupBy(PlayerBO::score).toSortedMap(compareByDescending { it })
        playersByScore.values.forEachIndexed { index, list -> list.forEach { it.ranking = index + 1 } }
        tournament.players = leaderboard.reversed()
    }

    override suspend fun findPlayer(tournamentId: ObjectId, playerName: String): PlayerBO {
        val tournament = this.findById(tournamentId)
        return tournament.findPlayer(playerName)
            ?: throw NotFoundException("Player $playerName is not registered to tournament $tournamentId")
    }

    override suspend fun deletePlayers(tournamentId: ObjectId): TournamentBO {
        val tournament = this.findById(tournamentId)
        tournament.players = listOf()
        return tournamentMapper.mapAround(tournamentRepository::update, tournament)
    }
}
