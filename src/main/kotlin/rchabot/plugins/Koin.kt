package rchabot.plugins

import io.ktor.application.*
import org.koin.core.module.Module
import rchabot.controller.player.playerControllerModule
import rchabot.controller.tournament.tournamentControllerModule
import rchabot.modules.playerServiceModule
import rchabot.modules.tournamentServiceModule
import rchabot.repository.mongoDBModule
import rchabot.repository.tournament.tournamentRepositoryModule


fun Application.configureKoin() {

    val miscModules = listOf<Module>(mongoDBModule)

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
