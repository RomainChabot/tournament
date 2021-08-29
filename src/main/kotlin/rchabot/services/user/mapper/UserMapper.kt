package rchabot.services.user.mapper

import org.mapstruct.Mapper
import rchabot.model.User
import rchabot.services.user.bo.UserBO

@Mapper
interface UserMapper {

    fun toBO(bo: User): UserBO
}