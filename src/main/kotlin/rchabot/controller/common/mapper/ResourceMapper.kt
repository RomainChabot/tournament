package rchabot.controller.common.mapper

/**
 * Resource mapper
 *
 * @param I BusinessObject
 * @param O Model
 */
interface ResourceMapper<I, O> {

    fun toResource(bo: I): O

    fun toBO(resource: O): I

    fun toResources(bos: List<I>): List<O> {
        return bos.map(this::toResource)
    }

    fun toBOs(resources: List<O>): List<I> {
        return resources.map(this::toBO)
    }

}


suspend infix fun <I, O> ResourceMapper<I, O>.mapResource(block: suspend () -> I): O {
    return toResource(block())
}

suspend infix fun <I, O> ResourceMapper<I, O>.mapBO(block: suspend () -> O): I {
    return toBO(block())
}
