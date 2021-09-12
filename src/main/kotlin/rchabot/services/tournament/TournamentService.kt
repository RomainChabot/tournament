package rchabot.services.tournament

import dev.forkhandles.result4k.Result4k
import org.bson.types.ObjectId
import rchabot.services.player.bo.PlayerBO
import rchabot.services.tournament.bo.TournamentBO

interface TournamentService {

    suspend fun create(name: String): Result4k<TournamentBO, Error>

    suspend fun findById(tournamentId: ObjectId): TournamentBO

    suspend fun delete(tournamentId: ObjectId)

    suspend fun addPlayer(tournamentId: ObjectId, playerBO: PlayerBO): Result4k<TournamentBO, Error>

    suspend fun updatePlayerPoints(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO

    suspend fun findPlayer(tournamentId: ObjectId, playerName: String): PlayerBO

    suspend fun deletePlayers(tournamentId: ObjectId): Result4k<TournamentBO, Error>
}
