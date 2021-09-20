package rchabot.common.page

import kotlin.reflect.KProperty

data class PageRequest(val page: Int, val size: Int, val sort: KProperty<*>) {

    fun numberToSkip() = page * size

}
