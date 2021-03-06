package rchabot.dao.tournament

import org.bson.types.ObjectId
import rchabot.common.page.Page
import rchabot.common.page.PageRequest
import rchabot.model.Player
import rchabot.model.Tournament

interface TournamentRepository {

    suspend fun findAll(pageRequest: PageRequest): Page<Tournament>

    suspend fun create(tournament: Tournament): Tournament

    suspend fun findById(id: ObjectId): Tournament

    suspend fun existsByName(name: String): Boolean

    suspend fun update(tournament: Tournament): Tournament

    suspend fun delete(id: ObjectId)

    suspend fun registerPlayer(tournamentId: ObjectId, player: Player)

    suspend fun updatePlayer(tournamentId: ObjectId, player: Player)

}
