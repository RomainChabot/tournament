package rchabot.repository.user

import org.koin.dsl.module
import rchabot.repository.user.impl.UserRepositoryImpl

val userRepositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}
