package rchabot.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

data class ErrorWithUri(val message: String, val uri: String)

fun Application.configureStatusPages() {

    install(StatusPages) {
        exception<NotFoundException> { cause ->
            call.respond(HttpStatusCode.NotFound, ErrorWithUri(cause.message!!, call.request.uri))
        }

        exception<BadRequestException> { cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message!!)
        }
    }

}
