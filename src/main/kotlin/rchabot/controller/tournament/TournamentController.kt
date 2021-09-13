package rchabot.controller.tournament

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.map
import org.bson.types.ObjectId
import rchabot.controller.player.mapper.PlayerResourceMapper
import rchabot.controller.player.resource.PlayerResource
import rchabot.controller.tournament.mapper.TournamentResourceMapper
import rchabot.controller.tournament.resource.TournamentResource
import rchabot.services.player.bo.PlayerBO
import rchabot.services.tournament.TournamentService
import rchabot.services.tournament.bo.TournamentBO


data class TournamentController(
    private val tournamentService: TournamentService,
    private val tournamentMapper: TournamentResourceMapper,
    private val playerMapper: PlayerResourceMapper,
) {

    suspend fun create(name: String): Result4k<TournamentResource, Error> {
        return tournamentService.create(name).map(tournamentMapper::toResource)
    }

    suspend fun read(tournamentId: String): TournamentResource {
        val result: TournamentBO = tournamentService.findById(ObjectId(tournamentId))
        return tournamentMapper.toResource(result)
    }

    suspend fun delete(tournamentId: String) {
        tournamentService.delete(ObjectId(tournamentId))
    }

    suspend fun deletePlayers(tournamentId: String): TournamentResource {
        return tournamentMapper.toResource(tournamentService.deletePlayers(ObjectId(tournamentId)))
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
        return tournamentMapper.toResource(
            tournamentService.updatePlayerScore(
                ObjectId(tournamentId),
                playerMapper.toBO(playerResource)
            )
        )
    }

    suspend fun getPlayer(tournamentId: String, playerName: String): PlayerResource {
        return playerMapper.toResource(tournamentService.findPlayer(ObjectId(tournamentId), playerName))
    }

}
