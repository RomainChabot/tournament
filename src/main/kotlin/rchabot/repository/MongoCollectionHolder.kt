package rchabot.repository

import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import rchabot.model.Tournament

class MongoCollectionHolder(client: CoroutineClient) {

    private val database = client.getDatabase("application")
    val tournament = database.getCollection<Tournament>("tournament")

}

val mongoDBModule = module {
    single<rchabot.repository.MongoCollectionHolder> {
        MongoCollectionHolder(
            KMongo.createClient(
                "mongodb://admin:admin@localhost:27017"
            ).coroutine
        )
    }
}
