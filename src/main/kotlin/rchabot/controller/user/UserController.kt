package rchabot.controller.user

import rchabot.controller.user.mapper.UserResourceMapper
import rchabot.controller.user.resource.UserResource
import rchabot.model.User
import rchabot.services.user.UserService

class UserController(private val userService: UserService, private val userResourceMapper: UserResourceMapper) {

    fun getUser(): UserResource {
        return userResourceMapper.toResource(userService.create(User("Maurice")))
    }

}



