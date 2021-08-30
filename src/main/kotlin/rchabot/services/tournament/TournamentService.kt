package rchabot.services.tournament

import org.bson.types.ObjectId
import rchabot.services.player.bo.PlayerBO
import rchabot.services.tournament.bo.TournamentBO

interface TournamentService {

    fun create(name: String): TournamentBO

    fun read(tournamentId: ObjectId): TournamentBO

    fun update(tournamentBO: TournamentBO): TournamentBO

    fun addPlayer(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO

    fun updatePlayerPoints(tournamentId: ObjectId, playerBO: PlayerBO): TournamentBO

    fun findPlayer(tournamentId: ObjectId, username: String): PlayerBO?
}
