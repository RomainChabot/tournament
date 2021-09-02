package rchabot.controller.tournament

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bson.types.ObjectId
import rchabot.controller.Controller
import rchabot.controller.player.mapper.PlayerResourceMapper
import rchabot.controller.player.resource.PlayerResource
import rchabot.controller.tournament.mapper.TournamentResourceMapper
import rchabot.controller.tournament.resource.TournamentResource
import rchabot.services.player.bo.PlayerBO
import rchabot.services.tournament.TournamentService
import rchabot.services.tournament.bo.TournamentBO

data class TournamentController(
    private val tournamentService: TournamentService,
    private val tournamentMapper: TournamentResourceMapper,
    private val playerMapper: PlayerResourceMapper,
) : Controller {

    fun create(name: String): TournamentResource {
        return tournamentMapper.toResource(tournamentService.create(name));
    }

    fun read(tournamentId: String): TournamentResource {
        val result: TournamentBO = tournamentService.read(ObjectId(tournamentId)) ?: TournamentBO("")
        return tournamentMapper.toResource(result)
    }

    fun delete(tournamentId: String): Unit {
        tournamentService.delete(ObjectId(tournamentId))
    }

    fun deletePlayers(tournamentId: String): TournamentResource {
        val result: TournamentBO = tournamentService.deletePlayers(ObjectId(tournamentId)) ?: TournamentBO("")
        return tournamentMapper.toResource(result)
    }

    fun addPlayer(tournamentId: String, playerName: String): TournamentResource {
        return tournamentMapper.toResource(
            tournamentService.addPlayer(
                ObjectId(tournamentId),
                PlayerBO(playerName, 0, null)
            )
        );
    }

    fun updatePlayerPoints(tournamentId: String, playerResource: PlayerResource): TournamentResource {
        return tournamentMapper.toResource(
            tournamentService.updatePlayerPoints(
                ObjectId(tournamentId),
                playerMapper.toBO(playerResource)
            )
        )
    }

    fun getPlayer(tournamentId: String, playerName: String): PlayerResource {
        val result: PlayerBO = tournamentService.findPlayer(ObjectId(tournamentId), playerName) ?: PlayerBO("", 0, null)
        return playerMapper.toResource(result)
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
                call.respond(read(tournamentId))
            }
            delete("{id}") {
                val tournamentId = call.parameters.get("id").toString()
                delete(tournamentId)
                call.respond(HttpStatusCode.OK, """Tournament deleted""")
            }
            post("{id}/players/add") {
                val tournamentId = call.parameters.get("id").toString()
                val playerName = call.request.queryParameters["playerName"]
                if (playerName == null) {
                    call.respond(HttpStatusCode.BadRequest, """Missing "playerName" query parameter""")
                } else {
                    call.respond(addPlayer(tournamentId, playerName))
                }
            }
            post("{id}/players/score") {
                val tournamentId = call.parameters.get("id").toString()
                call.respond(updatePlayerPoints(tournamentId, call.receive<PlayerResource>()))

            }
            get("{id}/players/{username}") {
                val tournamentId = call.parameters.get("id").toString()
                val username = call.parameters.get("username").toString()
                call.respond(getPlayer(tournamentId, username))
            }
            delete("{id}/players") {
                val tournamentId = call.parameters.get("id").toString()
                deletePlayers(tournamentId)
                call.respond(HttpStatusCode.OK, """Tournament players deleted""")
            }
        }
    }

}
