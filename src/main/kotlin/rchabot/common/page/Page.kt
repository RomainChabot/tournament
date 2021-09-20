package rchabot.common.page

data class Page<T>(val content: List<T>, val totalRecords: Long, val pageRequest: PageRequest) {

    fun <V> map(mappingFunction: (T) -> V): Page<V> {
        return Page(content.map(mappingFunction), totalRecords, pageRequest)
    }

}
