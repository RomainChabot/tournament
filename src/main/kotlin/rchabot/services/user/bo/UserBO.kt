package rchabot.services.user.bo

import org.bson.types.ObjectId

data class UserBO(val _id: ObjectId?, val username: String)
