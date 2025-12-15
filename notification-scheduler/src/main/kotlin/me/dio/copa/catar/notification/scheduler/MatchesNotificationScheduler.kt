package me.dio.copa.catar.notification.scheduler

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import me.dio.copa.catar.domain.model.Match
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

class MatchesNotificationScheduler @Inject constructor(
    private val context: Context
) {
    fun schedule(match: Match) {
        val workManager = WorkManager.getInstance(context)

        val now = LocalDateTime.now()
        val matchDateTime = match.date
        val delay = Duration.between(now, matchDateTime).toMillis()

        val data = Data.Builder()
            .putString(NotificationWorker.TEAM_1, match.team1.displayName)
            .putString(NotificationWorker.TEAM_2, match.team2.displayName)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        workManager.enqueueUniqueWork(
            match.id,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun unschedule(match: Match) {
        WorkManager.getInstance(context).cancelUniqueWork(match.id)
    }
}