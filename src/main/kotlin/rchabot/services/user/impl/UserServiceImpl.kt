package rchabot.services.user.impl

import rchabot.model.User
import rchabot.services.user.UserService
import rchabot.services.user.bo.UserBO
import rchabot.services.user.mapper.UserMapper

class UserServiceImpl(private val userMapper: UserMapper) : UserService {

    override fun create(userBO: User) : UserBO{
        return userMapper.toBO(userBO)
    }

}
