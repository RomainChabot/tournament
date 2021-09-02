package rchabot.modules

import org.koin.dsl.module
import rchabot.controller.tournament.TournamentController
import rchabot.controller.tournament.mapper.TournamentResourceMapper
import rchabot.controller.tournament.mapper.TournamentResourceMapperImpl


val tournamentControllerModule = module {
    single<TournamentController> { TournamentController(get(), get(), get()) }
    single<TournamentResourceMapper> { TournamentResourceMapperImpl() }
}
