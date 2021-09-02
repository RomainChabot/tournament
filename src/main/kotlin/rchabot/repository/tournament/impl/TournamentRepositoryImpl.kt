package rchabot.repository.tournament.impl

import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineCollection
import rchabot.model.Player
import rchabot.model.Tournament
import rchabot.repository.tournament.TournamentRepository

class TournamentRepositoryImpl(private val collection: CoroutineCollection<Tournament>) : TournamentRepository {

    override suspend fun create(tournament: Tournament): Tournament {
        collection.insertOne(tournament)
        return tournament
    }

    override suspend fun read(id: ObjectId): Tournament? {
        return collection.findOne(Tournament::_id eq id)
    }

    override suspend fun update(tournament: Tournament): Tournament {
        collection.updateOne(
            Tournament::_id eq tournament._id,
            set(Tournament::players setTo tournament.players)
        )
        return tournament
    }

    override suspend fun delete(id: ObjectId) {
        collection.deleteOne(Tournament::_id eq id)
    }

    override suspend fun addPlayer(tournamentId: ObjectId, player: Player) {
        collection.updateOne(
            Tournament::_id eq tournamentId,
            push(Tournament::players, player)
        )
    }

    override suspend fun updatePlayer(tournamentId: ObjectId, player: Player) {
        collection.updateOne(
            and(Tournament::_id eq tournamentId, (Tournament::players / Player::username) eq player.username),
            setValue(Tournament::players.posOp, player)
        )
    }


}
