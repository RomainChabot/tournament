package rchabot.plugins.impl

import io.ktor.application.*
import io.ktor.util.*

class MongoDB(configuration: Configuration) {

    // TODO Try some impl

    val url = configuration.url // Copies a snapshot of the mutable config into an immutable property.

    class Configuration {
        var url = "mongodb://admin:admin@localhost:27017" // Mutable property.
    }

    // Implements ApplicationFeature as a companion object.
    companion object Feature : ApplicationFeature<ApplicationCallPipeline, MongoDB.Configuration, MongoDB> {
        // Creates a unique key for the feature.
        override val key = AttributeKey<MongoDB>("MongoDB")

        // Code to execute when installing the plugin.
        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): MongoDB {

            // It is responsibility of the install code to call the `configure` method with the mutable configuration.
            val configuration = MongoDB.Configuration().apply(configure)

            // Create the plugin, providing the mutable configuration so the plugin reads it keeping an immutable copy of the properties.
            val feature = MongoDB(configuration)



            return feature
        }
    }

}
