package rchabot.services.user

import rchabot.model.User
import rchabot.services.user.bo.UserBO

interface UserService {

    fun create(userBO: User) : UserBO
}
