package rchabot.model

import org.bson.types.ObjectId
import rchabot.common.annotation.Default

data class Tournament @Default constructor(
    val _id: ObjectId?,
    val name: String,
    var players: List<Player> = listOf(),
    var nbPlayers: Int = 0
) {

    constructor(name: String) : this(_id = null, name = name)


}
