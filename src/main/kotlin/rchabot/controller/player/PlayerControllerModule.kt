package rchabot.modules

import org.koin.dsl.module
import rchabot.controller.player.mapper.PlayerResourceMapper
import rchabot.controller.player.mapper.PlayerResourceMapperImpl

val playerControllerModule = module {
    single<PlayerResourceMapper> { PlayerResourceMapperImpl() }
}
