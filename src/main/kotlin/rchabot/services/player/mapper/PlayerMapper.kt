package rchabot.services.player.mapper

import org.mapstruct.Mapper
import rchabot.model.Player
import rchabot.services.common.mapper.ModelMapper
import rchabot.services.player.bo.PlayerBO

@Mapper
interface PlayerMapper : ModelMapper<Player, PlayerBO> {

}
