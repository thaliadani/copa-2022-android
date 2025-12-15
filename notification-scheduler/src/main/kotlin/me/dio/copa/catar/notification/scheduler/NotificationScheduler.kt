package me.dio.copa.catar.notification.scheduler

import me.dio.copa.catar.domain.model.Match

interface NotificationScheduler {
    fun schedule(match: Match)
    fun unschedule(match: Match)
}