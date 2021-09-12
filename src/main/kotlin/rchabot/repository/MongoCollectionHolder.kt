package rchabot.repository

import io.ktor.application.*
import io.ktor.config.*
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import rchabot.model.Tournament

class MongoCollectionHolder(client: CoroutineClient) {

    private val database = client.getDatabase("application")
    val tournament = database.getCollection<Tournament>("tournament")

}

fun Application.mongoDBModule(applicationConfig: ApplicationConfig) = module {
    single<rchabot.repository.MongoCollectionHolder> {
        MongoCollectionHolder(
            KMongo.createClient(
                applicationConfig.property("ktor.mongodb.url").getString()
            ).coroutine
        )
    }
}
