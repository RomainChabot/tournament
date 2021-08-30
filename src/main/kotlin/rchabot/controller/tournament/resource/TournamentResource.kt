package rchabot.controller.tournament.resource

import org.bson.types.ObjectId
import rchabot.common.annotation.Default
import rchabot.services.player.bo.PlayerBO

class TournamentResource @Default constructor(
    val _id: ObjectId?,
    val name: String,
    val players: List<PlayerBO> = listOf()
) {

    constructor(name: String) : this(_id = null, name = name)
}
