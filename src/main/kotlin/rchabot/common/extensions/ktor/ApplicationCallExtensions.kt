package rchabot.common.extensions.ktor

import io.ktor.application.*
import io.ktor.features.*
import rchabot.common.regex.RegexConstants

fun ApplicationCall.getQueryParameterAsInt(parameter: String): Int {
    var parameterAsInt = 0
    this.request.queryParameters[parameter]?.let {
        if (it.matches(RegexConstants.INTEGER_REGEX)) {
            parameterAsInt = it.toInt()
        } else {
            throw BadRequestException(""""$parameter" query parameter is not an integer""")
        }
    } ?: throw BadRequestException(
        """"$parameter" query parameter is missing"""
    )
    return parameterAsInt
}

fun ApplicationCall.getQueryParameterAsString(parameter: String): String {
    var parameterAsString = ""
    this.request.queryParameters[parameter]?.let {
        if (it.isEmpty()) {
            throw BadRequestException(
                "$parameter should not be empty"
            )
        }
        parameterAsString = it
    } ?: throw BadRequestException(
        """"$parameter" query parameter is missing"""
    )
    return parameterAsString
}
