package rchabot.repository.user.impl

import rchabot.model.User
import rchabot.repository.MongoCollectionHolder
import rchabot.repository.user.UserRepository

class UserRepositoryImpl(private val dataSource: MongoCollectionHolder) : UserRepository {

    override fun create(user: User): User {
        return User(null, "")
    }
}
