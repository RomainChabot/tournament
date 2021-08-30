package rchabot.plugins

import io.ktor.application.*
import rchabot.modules.userControllerModule
import rchabot.modules.userServiceModule
import rchabot.repository.mongoDBModule
import rchabot.repository.user.userRepositoryModule


fun Application.configureKoin() {

    install(org.koin.ktor.ext.Koin) {
        modules(mongoDBModule, userRepositoryModule, userServiceModule, userControllerModule, userControllerModule)
    }

}
