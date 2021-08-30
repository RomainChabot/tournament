package rchabot.plugins

import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import rchabot.controller.tournament.TournamentController
import rchabot.controller.user.UserController

fun Application.configureRouting() {

    install(Routing)

    val userController: UserController by inject()
    val tournamentController: TournamentController by inject()

    routing {
        route("api") {
            userController.routeFrom(this)
            tournamentController.routeFrom(this)
        }
    }
}
