package rchabot.plugins

import io.ktor.application.*
import io.ktor.config.*
import org.koin.core.module.Module
import rchabot.controller.player.playerControllerModule
import rchabot.controller.tournament.tournamentControllerModule
import rchabot.modules.playerServiceModule
import rchabot.modules.tournamentServiceModule
import rchabot.repository.mongoDBModule
import rchabot.repository.tournament.tournamentRepositoryModule


fun Application.configureKoin(applicationConfig: ApplicationConfig) {

    val miscModules = listOf<Module>(mongoDBModule(applicationConfig))

    val tournamentModules = listOf<Module>(
        tournamentRepositoryModule,
        tournamentServiceModule,
        tournamentControllerModule
    )

    val playerModules = listOf<Module>(
        playerServiceModule,
        playerControllerModule
    )

    install(org.koin.ktor.ext.Koin) {
        modules(
            listOf(miscModules, tournamentModules, playerModules).flatten()
        )
    }

}
