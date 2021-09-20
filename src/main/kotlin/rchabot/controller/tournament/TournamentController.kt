package rchabot.controller.tournament

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.map
import org.bson.types.ObjectId
import rchabot.common.page.Page
import rchabot.common.page.PageRequest
import rchabot.controller.common.mapper.mapResource
import rchabot.controller.player.mapper.PlayerResourceMapper
import rchabot.controller.player.resource.PlayerResource
import rchabot.controller.tournament.mapper.TournamentResourceMapper
import rchabot.controller.tournament.resource.TournamentResource
import rchabot.model.Tournament
import rchabot.services.tournament.TournamentService


data class TournamentController(
    private val tournamentService: TournamentService,
    private val tournamentMapper: TournamentResourceMapper,
    private val playerMapper: PlayerResourceMapper,
) {

    suspend fun findAll(page: Int, size: Int): Page<TournamentResource> {
        return tournamentService.findAll(PageRequest(page, size, Tournament::_id))
            .map(tournamentMapper::toResource)
    }

    suspend fun create(name: String): Result4k<TournamentResource, Error> {
        return tournamentService.create(name).map(tournamentMapper::toResource)
    }

    suspend fun read(tournamentId: String): TournamentResource {
        return tournamentMapper mapResource {
            tournamentService.findById(ObjectId(tournamentId))
        }
    }

    suspend fun getLeaderboard(tournamentId: String): List<PlayerResource> {
        return tournamentService.getLeaderboard(ObjectId(tournamentId)).map(playerMapper::toResource)
    }

    suspend fun delete(tournamentId: String) {
        tournamentService.delete(ObjectId(tournamentId))
    }

    suspend fun deletePlayers(tournamentId: String): TournamentResource {
        return tournamentMapper mapResource {
            tournamentService.deletePlayers(ObjectId(tournamentId))
        }
    }

    suspend fun registerPlayer(tournamentId: String, playerName: String): Result4k<List<PlayerResource>, Error> {
        return tournamentService.registerPlayer(
            ObjectId(tournamentId),
            playerName
        ).map(playerMapper::toResources)
    }

    suspend fun updatePlayerPoints(
        tournamentId: String,
        playerResource: PlayerResource
    ): List<PlayerResource> {
        return tournamentService.updatePlayerScore(
            ObjectId(tournamentId),
            playerMapper.toBO(playerResource)
        ).map(playerMapper::toResource)
    }

    suspend fun getPlayer(tournamentId: String, playerName: String): PlayerResource {
        return playerMapper mapResource {
            tournamentService.findPlayer(ObjectId(tournamentId), playerName)
        }
    }

}
