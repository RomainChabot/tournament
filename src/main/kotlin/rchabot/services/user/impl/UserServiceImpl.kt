package rchabot.services.user.impl

import rchabot.repository.user.UserRepository
import rchabot.services.user.UserService
import rchabot.services.user.bo.UserBO
import rchabot.services.user.mapper.UserMapper

class UserServiceImpl(private val userRepository: UserRepository, private val userMapper: UserMapper) : UserService {

    override fun create(userBO: UserBO): UserBO {
        return userMapper.toBO(userRepository.create(userMapper.toModel(userBO)))
    }

}
