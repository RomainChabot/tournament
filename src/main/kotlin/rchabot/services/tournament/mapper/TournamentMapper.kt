package rchabot.services.tournament.mapper

import org.mapstruct.Mapper
import rchabot.model.Tournament
import rchabot.services.common.mapper.ModelMapper
import rchabot.services.tournament.bo.TournamentBO

@Mapper
interface TournamentMapper : ModelMapper<Tournament, TournamentBO>
