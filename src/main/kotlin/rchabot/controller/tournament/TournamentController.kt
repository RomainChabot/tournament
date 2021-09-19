package rchabot.controller.tournament

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.map
import org.bson.types.ObjectId
import rchabot.controller.common.mapper.mapResource
import rchabot.controller.player.mapper.PlayerResourceMapper
import rchabot.controller.player.resource.PlayerResource
import rchabot.controller.tournament.mapper.TournamentResourceMapper
import rchabot.controller.tournament.resource.TournamentResource
import rchabot.services.player.bo.PlayerBO
import rchabot.services.tournament.TournamentService


data class TournamentController(
    private val tournamentService: TournamentService,
    private val tournamentMapper: TournamentResourceMapper,
    private val playerMapper: PlayerResourceMapper,
) {

    suspend fun findAll(): Collection<TournamentResource> {
        return tournamentService.findAll().map(tournamentMapper::toResource)
    }

    suspend fun create(name: String): Result4k<TournamentResource, Error> {
        return tournamentService.create(name).map(tournamentMapper::toResource)
    }

    suspend fun read(tournamentId: String): TournamentResource {
        return tournamentMapper mapResource {
            tournamentService.findById(ObjectId(tournamentId))
        }
    }

    suspend fun delete(tournamentId: String) {
        tournamentService.delete(ObjectId(tournamentId))
    }

    suspend fun deletePlayers(tournamentId: String): TournamentResource {
        return tournamentMapper mapResource {
            tournamentService.deletePlayers(ObjectId(tournamentId))
        }
    }

    suspend fun addPlayer(tournamentId: String, playerName: String): Result4k<TournamentResource, Error> {
        return tournamentService.registerPlayer(
            ObjectId(tournamentId),
            PlayerBO(playerName, 0, null)
        ).map(tournamentMapper::toResource)
    }

    suspend fun updatePlayerPoints(
        tournamentId: String,
        playerResource: PlayerResource
    ): TournamentResource {
        return tournamentMapper mapResource {
            tournamentService.updatePlayerScore(
                ObjectId(tournamentId),
                playerMapper.toBO(playerResource)
            )
        }
    }

    suspend fun getPlayer(tournamentId: String, playerName: String): PlayerResource {
        return playerMapper mapResource {
            tournamentService.findPlayer(ObjectId(tournamentId), playerName)
        }
    }

}
