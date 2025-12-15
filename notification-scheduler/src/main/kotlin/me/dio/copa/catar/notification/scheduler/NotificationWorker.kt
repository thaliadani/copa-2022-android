package me.dio.copa.catar.notification.scheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.dio.copa.catar.notification.scheduler.R

class NotificationWorker(private val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    override fun doWork(): Result {
        val team1 = inputData.getString(TEAM_1)
        val team2 = inputData.getString(TEAM_2)

        if (team1.isNullOrEmpty() || team2.isNullOrEmpty()) {
            return Result.failure()
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Match Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Copa 2022")
            .setContentText("Hoje tem jogo: $team1 vs $team2")
            .setSmallIcon(R.drawable.ic_soccer)
            .build()

        notificationManager.notify(1, notification)

        return Result.success()
    }

    companion object {
        const val TEAM_1 = "team1"
        const val TEAM_2 = "team2"
        private const val CHANNEL_ID = "copa_2022_channel"
    }
}