package rchabot.controller.user

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import rchabot.controller.Controller
import rchabot.controller.user.mapper.UserResourceMapper
import rchabot.controller.user.resource.UserResource
import rchabot.services.user.UserService
import rchabot.services.user.bo.UserBO

class UserController(private val userService: UserService, private val userResourceMapper: UserResourceMapper) :
    Controller {

    fun getUser(): UserResource {
        return userResourceMapper.toResource(userService.create(UserBO(_id = null, username = "Maurice Monnot")))
    }

    override fun routeFrom(parent: Route): Route {
        return parent.route("user") {
            get("test") {
                call.respond(getUser())
            }
        }
    }
}



