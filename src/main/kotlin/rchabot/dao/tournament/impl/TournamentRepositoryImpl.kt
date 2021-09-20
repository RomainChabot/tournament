package rchabot.dao.tournament.impl

import io.ktor.features.*
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineCollection
import rchabot.common.page.Page
import rchabot.common.page.PageRequest
import rchabot.dao.tournament.TournamentRepository
import rchabot.model.Player
import rchabot.model.Tournament

class TournamentRepositoryImpl(private val collection: CoroutineCollection<Tournament>) : TournamentRepository {
    override suspend fun findAll(pageRequest: PageRequest): Page<Tournament> {
        val tournaments: List<Tournament> =
            collection.find().skip(pageRequest.numberToSkip()).limit(pageRequest.size).sort(ascending(pageRequest.sort))
                .toList()

        val totalRecords = collection.countDocuments()

        return Page(tournaments, totalRecords, pageRequest)
    }

    override suspend fun create(tournament: Tournament): Tournament {
        collection.insertOne(tournament)
        return tournament
    }

    override suspend fun findById(id: ObjectId): Tournament {
        return collection.findOne(Tournament::_id eq id)
            ?: throw NotFoundException("No tournament found with id $id")
    }

    override suspend fun existsByName(name: String): Boolean {
        return null != collection.findOne(Tournament::name eq name)
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

    override suspend fun registerPlayer(tournamentId: ObjectId, player: Player) {
        collection.updateOne(
            Tournament::_id eq tournamentId,
            push(Tournament::players, player)
        )
    }

    override suspend fun updatePlayer(tournamentId: ObjectId, player: Player) {
        collection.updateOne(
            and(Tournament::_id eq tournamentId, (Tournament::players / Player::playerName) eq player.playerName),
            setValue(Tournament::players.posOp, player)
        )
    }


}
