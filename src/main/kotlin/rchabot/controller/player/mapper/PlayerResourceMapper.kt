package rchabot.controller.player.mapper

import org.mapstruct.Mapper
import rchabot.controller.common.mapper.ResourceMapper
import rchabot.controller.player.resource.PlayerResource
import rchabot.services.player.bo.PlayerBO

@Mapper
interface PlayerResourceMapper : ResourceMapper<PlayerBO, PlayerResource> {
}
