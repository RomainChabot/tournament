package rchabot.controller.player.resource

import rchabot.common.annotation.Default

data class PlayerResource @Default constructor(val playerName: String, val score: Int = 0, val ranking: Int?) {
    constructor(playerName: String, score: Int) : this(playerName = playerName, score = score, ranking = null)
}
