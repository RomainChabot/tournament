package rchabot.services.common.mapper

/**
 * Model mapper
 *
 * @param I Model
 * @param O BusinessObject
 */
interface ModelMapper<I, O> {

    fun toBO(model: I): O

    fun toModel(bo: O): I

}

suspend infix fun <I, O> ModelMapper<I, O>.mapBO(block: suspend () -> I): O {
    return toBO(block())
}

suspend infix fun <I, O> ModelMapper<I, O>.mapModel(block: suspend () -> O): I {
    return toModel(block())
}

suspend fun <I, O> ModelMapper<I, O>.mapAround(block: suspend (I) -> I, input: O): O {
    return toBO(block(toModel(input)))
}

