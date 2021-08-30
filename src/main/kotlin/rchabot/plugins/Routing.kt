package rchabot.plugins

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import rchabot.controller.user.UserController

fun Application.configureRouting() {

    install(Routing)

    val userController: UserController by inject()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userController.routeFrom(this);
    }
}
