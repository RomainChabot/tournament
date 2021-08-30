package rchabot.services.tournament.bo

import rchabot.services.player.bo.PlayerBO

data class TournamentBO constructor(
    val name: String,
    val players: List<PlayerBO> = mutableListOf()
)
