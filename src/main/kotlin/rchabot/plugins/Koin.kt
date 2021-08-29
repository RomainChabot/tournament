package rchabot.plugins

import io.ktor.application.*
import rchabot.modules.userControllerModule
import rchabot.modules.userServiceModule


fun Application.configureKoin() {

    install(org.koin.ktor.ext.Koin) {
        modules(userServiceModule, userControllerModule)
    }

}
