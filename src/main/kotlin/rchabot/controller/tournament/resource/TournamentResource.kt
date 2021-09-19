package rchabot.controller.tournament.resource

import rchabot.common.annotation.Default
import rchabot.controller.player.resource.PlayerResource

class TournamentResource @Default constructor(
    val _id: String?,
    val name: String,
    val players: List<PlayerResource> = listOf()
) {

    constructor(name: String) : this(_id = null, name = name)
}
