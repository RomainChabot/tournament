package rchabot.controller.user.resource

import org.bson.types.ObjectId

data class UserResource(val _id: ObjectId?, val username: String)
