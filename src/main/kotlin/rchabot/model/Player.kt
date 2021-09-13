package rchabot.model

import rchabot.common.annotation.Default

data class Player @Default constructor(val playerName: String, val score: Int = 0, val ranking: Int?) {
    constructor(playerName: String) : this(playerName = playerName, score = 0, ranking = null)
}
