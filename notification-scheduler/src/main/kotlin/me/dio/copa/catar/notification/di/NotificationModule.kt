package me.dio.copa.catar.notification.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.dio.copa.catar.notification.scheduler.NotificationScheduler
import me.dio.copa.catar.notification.scheduler.NotificationSchedulerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    abstract fun bindsNotificationScheduler(impl: NotificationSchedulerImpl): NotificationScheduler
}