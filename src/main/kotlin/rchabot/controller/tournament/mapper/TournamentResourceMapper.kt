package rchabot.controller.tournament.mapper

import rchabot.controller.common.mapper.ResourceMapper
import rchabot.controller.tournament.resource.TournamentResource
import rchabot.services.tournament.bo.TournamentBO

interface TournamentResourceMapper : ResourceMapper<TournamentBO, TournamentResource>
