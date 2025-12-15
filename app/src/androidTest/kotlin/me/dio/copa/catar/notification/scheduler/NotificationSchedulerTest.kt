package me.dio.copa.catar.notification.scheduler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.model.Stadium
import me.dio.copa.catar.domain.model.Team
import me.dio.copa.catar.notification.scheduler.NotificationWorker.Companion.TEAM_1
import me.dio.copa.catar.notification.scheduler.NotificationWorker.Companion.TEAM_2
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class NotificationSchedulerTest {

    private lateinit var context: Context
    private lateinit var notificationScheduler: NotificationScheduler

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        notificationScheduler = NotificationSchedulerImpl(context)
    }

    @Test
    fun schedule_shouldEnqueueWorkRequest() {
        // 1. Create a fake Match object
        val fakeMatch = Match(
            id = UUID.randomUUID().toString(),
            name = "Fake Match",
            stadium = Stadium(name = "Fake Stadium", image = "fake_image"),
            team1 = Team(id = "1", displayName = "Team 1"),
            team2 = Team(id = "2", displayName = "Team 2"),
            date = LocalDateTime.now().plusMinutes(10),
            notificationEnabled = true
        )

        // 2. Call the schedule method
        notificationScheduler.schedule(fakeMatch)

        // 3. Verify that a WorkRequest with the correct tag has been enqueued
        val workManager = WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosByTag(fakeMatch.id).get()
        assert(workInfos.isNotEmpty())
    }

    @Test
    fun schedule_shouldShowNotification() {
        val fakeMatch = Match(
            id = UUID.randomUUID().toString(),
            name = "Fake Match",
            stadium = Stadium(name = "Fake Stadium", image = "fake_image"),
            team1 = Team(id = "1", displayName = "Team 1"),
            team2 = Team(id = "2", displayName = "Team 2"),
            date = LocalDateTime.now().plusMinutes(10),
            notificationEnabled = true
        )


        val inputData = Data.Builder()
            .putString(TEAM_1, fakeMatch.team1.displayName)
            .putString(TEAM_2, fakeMatch.team2.displayName)
            .build()

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(inputData)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(request).result.get()

        val workInfo = workManager.getWorkInfoById(request.id).get()
        assert(workInfo.state == androidx.work.WorkInfo.State.SUCCEEDED)
    }
}