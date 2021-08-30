package rchabot.plugins

import io.ktor.application.*
import org.koin.core.module.Module
import rchabot.modules.*
import rchabot.repository.mongoDBModule
import rchabot.repository.tournament.tournamentRepositoryModule
import rchabot.repository.user.userRepositoryModule


fun Application.configureKoin() {

    val miscModules = listOf<Module>(mongoDBModule)

    val userModules = listOf<Module>(
        userRepositoryModule,
        userServiceModule,
        userControllerModule
    )

    val tournamentModules = listOf<Module>(
        tournamentRepositoryModule,
        tournamentServiceModule,
        tournamentControllerModule
    )

    val playerModules = listOf<Module>(
        playerServiceModule
    )

    install(org.koin.ktor.ext.Koin) {
        modules(
            listOf(miscModules, userModules, tournamentModules, playerModules).flatten()
        )
    }

}
