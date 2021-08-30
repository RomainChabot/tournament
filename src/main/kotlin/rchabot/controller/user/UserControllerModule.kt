package rchabot.modules

import org.koin.dsl.module
import rchabot.controller.user.UserController
import rchabot.controller.user.mapper.UserResourceMapper
import rchabot.controller.user.mapper.UserResourceMapperImpl

val userControllerModule = module {
    single<UserController> { UserController(get(), get()) }
    single<UserResourceMapper> { UserResourceMapperImpl() }
}
