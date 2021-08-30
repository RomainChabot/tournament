package rchabot.services.user

import rchabot.services.user.bo.UserBO

interface UserService {

    fun create(userBO: UserBO): UserBO
}
