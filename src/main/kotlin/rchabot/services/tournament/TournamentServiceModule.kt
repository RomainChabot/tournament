package rchabot.modules

import org.koin.dsl.module
import rchabot.services.tournament.TournamentService
import rchabot.services.tournament.impl.TournamentServiceImpl
import rchabot.services.tournament.mapper.TournamentMapper
import rchabot.services.tournament.mapper.TournamentMapperImpl

val tournamentServiceModule = module {
    single<TournamentService> { TournamentServiceImpl(get(), get(), get()) }
    single<TournamentMapper> { TournamentMapperImpl() }
}
