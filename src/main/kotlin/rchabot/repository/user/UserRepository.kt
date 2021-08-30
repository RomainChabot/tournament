package rchabot.repository.user

import rchabot.model.User

interface UserRepository {

    fun create(user: User): User
}
