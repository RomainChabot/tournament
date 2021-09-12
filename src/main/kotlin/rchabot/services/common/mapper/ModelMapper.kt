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
