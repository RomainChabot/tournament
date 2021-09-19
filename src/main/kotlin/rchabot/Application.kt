package rchabot

import io.ktor.application.*
import io.ktor.config.*
import rchabot.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

operator fun ApplicationConfig.get(key: String) = this.propertyOrNull(key)?.getString()

fun Application.module() {

    configureKoin(environment.config)
    configureRouting()
    configureSerialization()
    configureStatusPages()
    configureCors()

}


