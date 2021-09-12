package rchabot.services.tournament.bo

import org.bson.types.ObjectId
import rchabot.common.annotation.Default
import rchabot.services.player.bo.PlayerBO

data class TournamentBO @Default constructor(
    val _id: ObjectId?,
    val name: String,
    var players: List<PlayerBO> = listOf<PlayerBO>()
) {
    constructor(name: String) : this(_id = null, name = name)

    fun findPlayer(playerName: String): PlayerBO? =
        players.find { it.playerName == playerName }

    fun playerExists(playerName: String): Boolean = findPlayer(playerName) != null
}
