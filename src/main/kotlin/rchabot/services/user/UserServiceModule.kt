package rchabot.modules

import org.koin.dsl.module
import rchabot.services.user.UserService
import rchabot.services.user.impl.UserServiceImpl
import rchabot.services.user.mapper.UserMapper
import rchabot.services.user.mapper.UserMapperImpl

val userServiceModule = module {
    single<UserService> { UserServiceImpl(get()) }
    single<UserMapper> { UserMapperImpl() }
}
