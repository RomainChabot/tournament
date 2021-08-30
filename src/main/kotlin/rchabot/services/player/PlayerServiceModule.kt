package rchabot.modules

import org.koin.dsl.module
import rchabot.services.player.mapper.PlayerMapper
import rchabot.services.player.mapper.PlayerMapperImpl

val playerServiceModule = module {
    single<PlayerMapper> { PlayerMapperImpl() }
}
