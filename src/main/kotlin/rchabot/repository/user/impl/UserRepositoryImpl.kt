package rchabot.repository.user.impl

import org.litote.kmongo.getCollection
import rchabot.model.User
import rchabot.repository.MongoDBDataSource
import rchabot.repository.user.UserRepository

class UserRepositoryImpl(private val dataSource: MongoDBDataSource) : UserRepository {

    override fun create(user: User): User {
        val collection = dataSource.database.getCollection<User>("user")
        return user
    }
}
