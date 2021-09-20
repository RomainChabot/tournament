package rchabot.controller.tournament.resource

import rchabot.common.annotation.Default

class TournamentResource @Default constructor(
    val _id: String?,
    val name: String,
    val nbPlayers: Int = 0
) {

    constructor(name: String) : this(_id = null, name = name)
}
