package rchabot.services.player.bo

import rchabot.common.annotation.Default

data class PlayerBO @Default constructor(val playerName: String, var score: Int = 0, var ranking: Int? = null) {
    constructor(playerName: String) : this(playerName = playerName, score = 0, ranking = null)
}
