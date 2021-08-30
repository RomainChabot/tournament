package rchabot.repository

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.koin.dsl.module
import org.litote.kmongo.KMongo

class MongoDBDataSource(val client: MongoClient) {

    val database: MongoDatabase = client.getDatabase("application")

}

val mongoDBModule = module {
    single<MongoDBDataSource> {
        MongoDBDataSource(
            KMongo.createClient(
                "mongodb://admin:admin@localhost:27017"
            )
        )
    }
}
