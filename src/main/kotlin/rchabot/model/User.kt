package rchabot.model

import org.bson.types.ObjectId

data class User(val _id: ObjectId?, val playerName: String)
