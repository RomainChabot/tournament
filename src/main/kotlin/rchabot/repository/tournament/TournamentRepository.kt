package rchabot.repository.tournament

import org.bson.types.ObjectId
import rchabot.model.Player
import rchabot.model.Tournament

interface TournamentRepository {

    fun create(tournament: Tournament): Tournament

    fun read(id: ObjectId): Tournament?

    fun update(tournament: Tournament): Tournament

    fun delete(id: ObjectId): Unit

    fun addPlayer(tournamentId: ObjectId, player: Player): Unit

    fun updatePlayer(tournamentId: ObjectId, player: Player): Unit

}
