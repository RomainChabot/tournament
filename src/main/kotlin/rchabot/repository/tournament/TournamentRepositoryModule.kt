package rchabot.repository.tournament

import org.koin.dsl.module
import rchabot.repository.tournament.impl.TournamentRepositoryImpl

val tournamentRepositoryModule = module {
    single<TournamentRepository> { TournamentRepositoryImpl(get()) }
}
