package rchabot.controller.common.mapper

/**
 * Resource mapper
 *
 * @param I BusinessObject
 * @param O Model
 */
interface ResourceMapper<I, O> {

    fun toResource(bo: I): O

    fun toBO(bo: O): I

}
