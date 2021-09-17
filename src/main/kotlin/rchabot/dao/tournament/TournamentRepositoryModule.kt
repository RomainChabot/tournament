package rchabot.dao.tournament

import org.koin.dsl.module
import rchabot.dao.MongoCollectionHolder
import rchabot.dao.tournament.impl.TournamentRepositoryImpl

val tournamentRepositoryModule = module {
    single<TournamentRepository> { TournamentRepositoryImpl((get() as MongoCollectionHolder).tournament) }
}
