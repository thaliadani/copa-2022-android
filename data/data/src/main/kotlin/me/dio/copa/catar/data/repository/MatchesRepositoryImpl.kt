package me.dio.copa.catar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import me.dio.copa.catar.data.source.MatchesDataSource
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.notification.scheduler.NotificationScheduler
import javax.inject.Inject

class MatchesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MatchesDataSource.Remote,
    private val localDataSource: MatchesDataSource.Local,
    private val notificationScheduler: NotificationScheduler,
) : MatchesRepository {

    private var cachedMatches: List<Match>? = null

    override suspend fun getMatches(): Flow<List<Match>> {
        return flow {
            val matches = cachedMatches ?: remoteDataSource.getMatches().also {
                cachedMatches = it
            }
            emit(matches)
        }.combine(localDataSource.getActiveNotificationIds()) { matches, ids ->
            matches.map { match ->
                match.copy(notificationEnabled = ids.contains(match.id))
            }
        }
    }

    override suspend fun enableNotificationFor(id: String) {
        findMatch(id)?.also { match ->
            notificationScheduler.schedule(match.copy(notificationEnabled = true))
            localDataSource.enableNotificationFor(id)
        }
    }

    override suspend fun disableNotificationFor(id: String) {
        findMatch(id)?.also { match ->
            notificationScheduler.unschedule(match.copy(notificationEnabled = false))
            localDataSource.disableNotificationFor(id)
        }
    }

    private suspend fun findMatch(id: String): Match? {
        val matches = cachedMatches ?: remoteDataSource.getMatches().also { cachedMatches = it }
        return matches.find { it.id == id }
    }
}
