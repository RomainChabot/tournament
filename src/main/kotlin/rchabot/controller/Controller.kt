package rchabot.controller

import io.ktor.routing.*

interface Controller {

    /**
     * Route from parent
     *
     * @param parent
     * @return Route defined by Controller
     */
    fun routeFrom(parent: Route): Route;
}
