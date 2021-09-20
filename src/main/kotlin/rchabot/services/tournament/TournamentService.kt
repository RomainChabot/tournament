package rchabot.services.tournament

import dev.forkhandles.result4k.Result4k
import org.bson.types.ObjectId
import rchabot.common.page.Page
import rchabot.common.page.PageRequest
import rchabot.services.player.bo.PlayerBO
import rchabot.services.tournament.bo.TournamentBO

interface TournamentService {

    suspend fun findAll(pageRequest: PageRequest): Page<TournamentBO>

    suspend fun create(name: String): Result4k<TournamentBO, Error>

    suspend fun findById(tournamentId: ObjectId): TournamentBO

    suspend fun delete(tournamentId: ObjectId)

    suspend fun getLeaderboard(tournamentId: ObjectId): List<PlayerBO>

    suspend fun registerPlayer(tournamentId: ObjectId, playerName: String): Result4k<List<PlayerBO>, Error>

    suspend fun updatePlayerScore(tournamentId: ObjectId, player: PlayerBO): List<PlayerBO>

    suspend fun findPlayer(tournamentId: ObjectId, playerName: String): PlayerBO

    suspend fun deletePlayers(tournamentId: ObjectId): TournamentBO
}
