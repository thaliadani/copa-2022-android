// in /notification-scheduler/src/main/kotlin/me/dio/copa/catar/notification/scheduler/NotificationSchedulerImpl.kt

package me.dio.copa.catar.notification.scheduler

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.notification.scheduler.NotificationWorker.Companion.TEAM_1
import me.dio.copa.catar.notification.scheduler.NotificationWorker.Companion.TEAM_2
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

class NotificationSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationScheduler {
    override fun schedule(match: Match) {
        val workManager = WorkManager.getInstance(context)

        // FIX: Use the correct property from your Team model.
        // Replace 'displayName' with the actual property name if it's different.
        val inputData = Data.Builder()
            .putString(TEAM_1, match.team1.displayName) // CORRECTED
            .putString(TEAM_2, match.team2.displayName) // CORRECTED
            .build()

        val notificationTime = match.date
        val delay = Duration.between(LocalDateTime.now(), notificationTime).toMillis()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(inputData)
            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
            .addTag(match.id)
            .build()

        workManager.enqueue(workRequest)
    }

    override fun unschedule(match: Match) {
        WorkManager.getInstance(context).cancelAllWorkByTag(match.id)
    }
}
    