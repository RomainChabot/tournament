package rchabot.modules

import org.koin.dsl.module
import rchabot.controller.user.UserController
import rchabot.controller.user.mapper.UserResourceMapper
import rchabot.controller.user.mapper.UserResourceMapperImpl
import rchabot.services.user.UserService
import rchabot.services.user.impl.UserServiceImpl

val userControllerModule = module {
    single<UserController> { UserController(get(), get()) }
    single<UserResourceMapper> { UserResourceMapperImpl() }
    single<UserService> { UserServiceImpl(get()) }
}
