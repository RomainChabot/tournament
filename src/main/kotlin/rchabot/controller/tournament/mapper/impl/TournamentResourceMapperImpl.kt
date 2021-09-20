package rchabot.controller.tournament.mapper.impl

import org.bson.types.ObjectId
import rchabot.controller.player.mapper.PlayerResourceMapper
import rchabot.controller.tournament.mapper.TournamentResourceMapper
import rchabot.controller.tournament.resource.TournamentResource
import rchabot.services.tournament.bo.TournamentBO

class TournamentResourceMapperImpl(private val playerMapper: PlayerResourceMapper) : TournamentResourceMapper {

    override fun toResource(bo: TournamentBO): TournamentResource {

        return TournamentResource(bo._id.toString(), bo.name, bo.players.size)
    }

    override fun toBO(resource: TournamentResource): TournamentBO {
        return TournamentBO(ObjectId(resource._id), resource.name)
    }
}
