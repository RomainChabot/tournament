package rchabot.services.tournament.impl

import org.bson.types.ObjectId
import rchabot.common.exception.ModelNotFoundException
import rchabot.model.Tournament
import rchabot.repository.tournament.TournamentRepository
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

    override suspend fun create(name: String): TournamentBO {
        return tournamentMapper.toBO(tournamentRepository.create(Tournament(name = name)))
    }

    override suspend fun read(tournamentId: ObjectId): TournamentBO {
        val tournament = tournamentRepository.read(tournamentId)
            ?: throw ModelNotFoundException("No tournament found with id ${tournamentId}")
        return tournamentMapper.toBO(tournament)
    }

    override suspend fun update(tournamentBO: TournamentBO): TournamentBO {
        return tournamentMapper.toBO(tournamentRepository.update(tournamentMapper.toModel(tournamentBO)))
    }

    override suspend fun delete(tournamentId: ObjectId) {
        tournamentRepository.delete(tournamentId)
    }

    override suspend fun addPlayer(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO {
        tournamentRepository.addPlayer(tournamentId, playerMapper.toModel(playerBO))
        return this.read(tournamentId)
    }

    override suspend fun updatePlayerPoints(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO {
        // TODO Handle player not found
        val tournament = this.read(tournamentId)
        val player = tournament.players.find { it.username == playerBO.username }!!
        player.score = playerBO.score
        updatePLayersRanking(tournament)
        return tournamentMapper.toBO(tournamentRepository.update(tournamentMapper.toModel(tournament)))
    }

    private fun updatePLayersRanking(tournament: TournamentBO) {
        val updated = tournament.players.sortedBy(PlayerBO::score)
        val playersByScore = tournament.players.groupBy(PlayerBO::score).toSortedMap(compareByDescending { it })
        playersByScore.values.forEachIndexed { index, list -> list.forEach { it.ranking = index + 1 } }
        tournament.players = updated
    }

    override suspend fun findPlayer(tournamentId: ObjectId, username: String): PlayerBO? {
        val tournament = this.read(tournamentId)
        return tournament.players.find { it.username == username }
    }

    override suspend fun deletePlayers(tournamentId: ObjectId): TournamentBO {
        val tournament = this.read(tournamentId)
        tournament.players = listOf()
        return tournamentMapper.toBO(tournamentRepository.update(tournamentMapper.toModel(tournament)))
    }
}
