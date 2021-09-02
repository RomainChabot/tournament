package rchabot.services.tournament

import org.bson.types.ObjectId
import rchabot.services.player.bo.PlayerBO
import rchabot.services.tournament.bo.TournamentBO

interface TournamentService {

    suspend fun create(name: String): TournamentBO

    suspend fun read(tournamentId: ObjectId): TournamentBO?

    suspend fun update(tournamentBO: TournamentBO): TournamentBO

    suspend fun delete(tournamentId: ObjectId)

    suspend fun addPlayer(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO

    suspend fun updatePlayerPoints(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO

    suspend fun findPlayer(tournamentId: ObjectId, username: String): PlayerBO?

    suspend fun deletePlayers(tournamentId: ObjectId): TournamentBO?
}
