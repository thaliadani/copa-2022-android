package me.dio.copa.catar.remote.mapper

import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.model.Stadium
import me.dio.copa.catar.domain.model.Team
import me.dio.copa.catar.remote.model.MatchRemote
import me.dio.copa.catar.remote.model.StadiumRemote
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

internal fun List<MatchRemote>.toDomain() = map { it.toDomain() }

fun MatchRemote.toDomain(): Match {
    return Match(
        id = "$team1-$team2",
        name = name,
        stadium = stadium.toDomain(),
        team1 = team1.toTeam(),
        team2 = team2.toTeam(),
        date = date.toLocalDateTime(),
    )
}

private fun Date.toLocalDateTime(): LocalDateTime {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

private fun String.toTeam(): Team {
    return Team(
        id = this,
        displayName = Locale("", this).isO3Country
    )
}

fun StadiumRemote.toDomain(): Stadium {
    return Stadium(
        name = name,
        image = image
    )
}
