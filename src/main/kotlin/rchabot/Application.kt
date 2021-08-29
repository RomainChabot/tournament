package rchabot

import io.ktor.application.*
import rchabot.plugins.configureKoin
import rchabot.plugins.configureRouting
import rchabot.plugins.configureSerialization

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {

    configureKoin()
    configureRouting()
    configureSerialization()

}
