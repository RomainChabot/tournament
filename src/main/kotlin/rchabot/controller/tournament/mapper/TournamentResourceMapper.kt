package rchabot.controller.tournament.mapper

import org.mapstruct.Mapper
import rchabot.controller.common.mapper.ResourceMapper
import rchabot.controller.tournament.resource.TournamentResource
import rchabot.services.tournament.bo.TournamentBO

@Mapper
interface TournamentResourceMapper : ResourceMapper<TournamentBO, TournamentResource> {
}
