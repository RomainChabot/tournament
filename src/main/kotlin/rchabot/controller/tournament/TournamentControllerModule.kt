package rchabot.controller.tournament

import org.koin.dsl.module
import rchabot.controller.tournament.mapper.TournamentResourceMapper
import rchabot.controller.tournament.mapper.impl.TournamentResourceMapperImpl


val tournamentControllerModule = module {
    single<TournamentController> { TournamentController(get(), get(), get()) }
    single<TournamentResourceMapper> { TournamentResourceMapperImpl(get()) }
}
