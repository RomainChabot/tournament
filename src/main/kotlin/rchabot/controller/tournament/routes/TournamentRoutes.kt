package rchabot.controller.tournament.routes

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.get
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import rchabot.controller.player.resource.PlayerResource
import rchabot.controller.tournament.TournamentController


fun Application.tournamentRoutes(parent: Route) {

    val tournamentController: TournamentController by inject()

    fun Route.createTournament() {
        post("") {
            call.request.queryParameters["name"]?.let {
                when (val result = tournamentController.create(it)) {
                    is Failure -> call.respond(HttpStatusCode.OK, result.reason.message!!)
                    is Success -> call.respond(HttpStatusCode.Created, result.get())
                }
            } ?: call.respond(HttpStatusCode.BadRequest, """"name" query parameter is missing""")
        }
    }

    fun Route.readTournament() {
        get("{id}") {
            val tournamentId = call.parameters["id"].toString()
            call.respond(tournamentController.read(tournamentId))
        }
    }

    fun Route.deleteTournament() {
        delete("{id}") {
            val tournamentId = call.parameters["id"].toString()
            tournamentController.delete(tournamentId)
            call.respond(HttpStatusCode.OK, """Tournament $tournamentId deleted""")
        }
    }

    fun Route.getTournamentLeaderboard() {
        get("{id}/leaderboard") {
            val tournamentId = call.parameters["id"].toString()
            call.respond(tournamentController.read(tournamentId).players)
        }
    }

    fun Route.addTournamentPlayer() {
        post("") {
            val tournamentId = call.parameters["id"].toString()
            call.request.queryParameters["playerName"]?.let {
                when (val result = tournamentController.addPlayer(tournamentId, it)) {
                    is Failure -> call.respond(HttpStatusCode.OK, result.reason.message!!)
                    is Success -> call.respond(result.get())
                }
            } ?: call.respond(HttpStatusCode.BadRequest, """"playerName" query parameter is missing""")

        }
    }

    fun Route.updateTournamentPlayerScore() {
        post("{playerName}/scores") {
            val tournamentId = call.parameters["id"].toString()
            val playerName = call.parameters["playerName"].toString()
            call.request.queryParameters["score"]?.let {
                if (it.matches(Regex("""\d+"""))) {
                    call.respond(
                        tournamentController.updatePlayerPoints(
                            tournamentId,
                            PlayerResource(playerName = playerName, score = it.toInt())
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.BadRequest, """"score" query parameter is not an integer""")
                }
            }
        }
    }

    fun Route.getTournamentPlayer() {
        get("{playerName}") {
            val tournamentId = call.parameters["id"].toString()
            val playerName = call.parameters["playerName"].toString()
            call.respond(tournamentController.getPlayer(tournamentId, playerName))
        }
    }

    fun Route.deleteTournamentPlayers() {
        delete("") {
            val tournamentId = call.parameters["id"].toString()
            call.respond(tournamentController.deletePlayers(tournamentId))
        }
    }

    parent.route("tournament") {
        createTournament()
        readTournament()
        getTournamentLeaderboard()
        deleteTournament()
        route("{id}/players") {
            addTournamentPlayer()
            updateTournamentPlayerScore()
            getTournamentPlayer()
            deleteTournamentPlayers()
        }
    }

}
