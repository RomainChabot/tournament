package rchabot.services.user.mapper

import org.mapstruct.Mapper
import rchabot.model.User
import rchabot.services.common.mapper.ModelMapper
import rchabot.services.user.bo.UserBO

@Mapper
interface UserMapper : ModelMapper<User, UserBO> {

}
