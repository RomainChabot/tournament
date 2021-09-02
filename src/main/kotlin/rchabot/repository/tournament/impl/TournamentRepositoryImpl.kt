package rchabot.repository.tournament.impl

import org.bson.types.ObjectId
import org.litote.kmongo.*
import rchabot.model.Player
import rchabot.model.Tournament
import rchabot.repository.MongoDBDataSource
import rchabot.repository.tournament.TournamentRepository

class TournamentRepositoryImpl(private val dataSource: MongoDBDataSource) : TournamentRepository {

    // TODO Better handling of collection

    override fun create(tournament: Tournament): Tournament {
        val collection = dataSource.database.getCollection<Tournament>("tournament")
        collection.insertOne(tournament);
        return tournament
    }

    override fun read(id: ObjectId): Tournament? {
        val collection = dataSource.database.getCollection<Tournament>("tournament")
        return collection.findOne(Tournament::_id eq id)
    }

    override fun update(tournament: Tournament): Tournament {
        val collection = dataSource.database.getCollection<Tournament>("tournament")
        collection.updateOne(
            Tournament::_id eq tournament._id,
            set(Tournament::players setTo tournament.players)
        )
        return tournament
    }

    override fun delete(id: ObjectId) {
        val collection = dataSource.database.getCollection<Tournament>("tournament")
        collection.deleteOne(Tournament::_id eq id)
    }

    override fun addPlayer(tournamentId: ObjectId, player: Player): Unit {
        val collection = dataSource.database.getCollection<Tournament>("tournament")
        collection.updateOne(
            Tournament::_id eq tournamentId,
            push(Tournament::players, player)
        )
    }

    override fun updatePlayer(tournamentId: ObjectId, player: Player): Unit {
        val collection = dataSource.database.getCollection<Tournament>("tournament")
        collection.updateOne(
            and(Tournament::_id eq tournamentId, (Tournament::players / Player::username) eq player.username),
            setValue(Tournament::players.posOp, player)
        )
    }


}
