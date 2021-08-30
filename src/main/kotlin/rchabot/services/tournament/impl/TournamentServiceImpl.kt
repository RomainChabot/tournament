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

    override fun create(name: String): TournamentBO {
        return tournamentMapper.toBO(tournamentRepository.create(Tournament(name = name)))
    }

    override fun read(tournamentId: ObjectId): TournamentBO {
        val tournament = tournamentRepository.read(tournamentId)
            ?: throw ModelNotFoundException("No tournament found with id ${tournamentId}")
        return tournamentMapper.toBO(tournament);
    }

    override fun update(tournamentBO: TournamentBO): TournamentBO {
        return tournamentMapper.toBO(tournamentRepository.update(tournamentMapper.toModel(tournamentBO)))
    }

    override fun addPlayer(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO {
        tournamentRepository.addPlayer(tournamentId, playerMapper.toModel(playerBO))
        return this.read(tournamentId)
    }

    override fun updatePlayerPoints(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO {
        TODO("Not yet implemented")
    }

    override fun findPlayer(tournamentId: ObjectId, username: String): PlayerBO? {
        val tournament = this.read(tournamentId);
        return tournament.players.find { it.username == username }
    }
}
