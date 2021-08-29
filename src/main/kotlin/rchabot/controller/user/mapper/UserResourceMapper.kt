package rchabot.controller.user.mapper

import org.mapstruct.Mapper
import rchabot.controller.user.resource.UserResource
import rchabot.services.user.bo.UserBO

@Mapper
interface UserResourceMapper {

    fun toResource(userBO: UserBO): UserResource
}
