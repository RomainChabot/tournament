package rchabot.repository.tournament

import org.bson.types.ObjectId
import rchabot.model.Player
import rchabot.model.Tournament

interface TournamentRepository {

    suspend fun create(tournament: Tournament): Tournament

    suspend fun read(id: ObjectId): Tournament?

    suspend fun update(tournament: Tournament): Tournament

    suspend fun delete(id: ObjectId)

    suspend fun addPlayer(tournamentId: ObjectId, player: Player)

    suspend fun updatePlayer(tournamentId: ObjectId, player: Player)

}
