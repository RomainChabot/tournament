package rchabot.controller.tournament.routes

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.get
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import rchabot.common.extensions.ktor.getQueryParameterAsInt
import rchabot.common.extensions.ktor.getQueryParameterAsString
import rchabot.controller.player.resource.PlayerResource
import rchabot.controller.tournament.TournamentController

fun Application.tournamentRoutes(parent: Route) {

    val tournamentController: TournamentController by inject()

    fun Route.findAllTournament() {
        get("") {
            val page = call.getQueryParameterAsInt("page")
            val size = call.getQueryParameterAsInt("size")
            call.respond(tournamentController.findAll(page = page, size = size))
        }
    }

    fun Route.createTournament() {
        post("") {
            val name = call.getQueryParameterAsString("name")
            when (val result = tournamentController.create(name)) {
                is Failure -> call.respond(HttpStatusCode.BadRequest, result.reason.message!!)
                is Success -> call.respond(HttpStatusCode.Created, result.get())
            }
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
            call.respond(HttpStatusCode.OK)
        }
    }

    fun Route.getTournamentLeaderboard() {
        get("{id}/leaderboard") {
            val tournamentId = call.parameters["id"].toString()
            call.respond(tournamentController.getLeaderboard(tournamentId))
        }
    }

    fun Route.registerTournamentPlayer() {
        post("") {
            val tournamentId = call.parameters["id"].toString()
            val playerName = call.getQueryParameterAsString("playerName")
            when (val result = tournamentController.registerPlayer(tournamentId, playerName)) {
                is Failure -> call.respond(HttpStatusCode.BadRequest, result.reason.message!!)
                is Success -> call.respond(result.get())
            }
        }
    }

    fun Route.updateTournamentPlayerScore() {
        put("{playerName}/scores") {
            val tournamentId = call.parameters["id"].toString()
            val playerName = call.parameters["playerName"].toString()
            val score = call.getQueryParameterAsInt("score")
            call.respond(
                tournamentController.updatePlayerPoints(
                    tournamentId,
                    PlayerResource(playerName = playerName, score = score)
                )
            )
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
        findAllTournament()
        createTournament()
        readTournament()
        getTournamentLeaderboard()
        deleteTournament()
        route("{id}/players") {
            registerTournamentPlayer()
            updateTournamentPlayerScore()
            getTournamentPlayer()
            deleteTournamentPlayers()
        }
    }

}
