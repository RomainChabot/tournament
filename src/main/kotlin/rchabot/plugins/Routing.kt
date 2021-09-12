package rchabot.plugins

import io.ktor.application.*
import io.ktor.routing.*
import rchabot.routes.tournament.tournamentRoutes

fun Application.configureRouting() {

    install(Routing)

    routing {
        route("api") {
            tournamentRoutes(this)
        }
    }
}
