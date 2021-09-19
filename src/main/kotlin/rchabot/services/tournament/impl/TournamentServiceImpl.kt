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

    override suspend fun registerPlayer(tournamentId: ObjectId, playerBO: PlayerBO): Result4k<TournamentBO, Error> {
        val tournament = this.findById(tournamentId)
        if (tournament.existsPlayer(playerBO.playerName)) {
            return Failure(Error("Player ${playerBO.playerName} is already registered"))
        }
        tournamentRepository.registerPlayer(tournamentId, playerMapper.toModel(playerBO))
        return Success(this.updatePlayerScore(tournamentId, playerBO))
    }

    override suspend fun updatePlayerScore(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO {
        val tournament = this.findById(tournamentId)
        tournament.findPlayer(playerBO.playerName)?.let {
            it.score = playerBO.score
            updatePlayersRanking(tournament)
            return tournamentMapper.mapAround(tournamentRepository::update, tournament)
        } ?: throw NotFoundException("Player ${playerBO.playerName} is not registered to tournament $tournamentId")
    }

    private fun updatePlayersRanking(tournament: TournamentBO) {
        val updated = tournament.players.sortedBy(PlayerBO::score)
        val playersByScore = tournament.players.groupBy(PlayerBO::score).toSortedMap(compareByDescending { it })
        playersByScore.values.forEachIndexed { index, list -> list.forEach { it.ranking = index + 1 } }
        tournament.players = updated.reversed()
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
