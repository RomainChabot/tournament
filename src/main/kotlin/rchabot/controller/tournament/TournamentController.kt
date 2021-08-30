package rchabot.controller.tournament

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bson.types.ObjectId
import rchabot.controller.Controller
import rchabot.controller.tournament.mapper.TournamentResourceMapper
import rchabot.controller.tournament.resource.TournamentResource
import rchabot.services.tournament.TournamentService

data class TournamentController(
    private val tournamentService: TournamentService,
    private val tournamentMapper: TournamentResourceMapper
) : Controller {

    fun create(name: String): TournamentResource {
        return tournamentMapper.toResource(tournamentService.create(name));
    }

    override fun routeFrom(parent: Route): Route {
        return parent.route("tournament") {
            post("") {
                val tournamentName = call.request.queryParameters["name"]
                if (tournamentName == null) {
                    call.respond(HttpStatusCode.BadRequest, """Missing "name" query parameter""")
                } else {
                    call.respond(create(tournamentName))
                }
            }
            get("{id}") {
                val tournamentId = call.parameters.get("id").toString()
                call.respond(tournamentMapper.toResource(tournamentService.read(ObjectId(tournamentId))))
            }
            post("{id}/players") {
                val tournamentId = call.parameters.get("id").toString()
                val playerName = call.request.queryParameters["playerName"]
                if (playerName == null) {
                    call.respond(HttpStatusCode.BadRequest, """Missing "name" query parameter""")
                } else {
                    call.respond(tournamentMapper.toResource(tournamentService.read(ObjectId(tournamentId))))
                }
            }
            post("{id}/players/{username}") {
                val tournamentId = call.parameters.get("id").toString()

            }
            get("{id}/players/{username}") {
                val tournamentId = call.parameters.get("id").toString()
                val username = call.parameters.get("username").toString()


            }
        }
    }

}
