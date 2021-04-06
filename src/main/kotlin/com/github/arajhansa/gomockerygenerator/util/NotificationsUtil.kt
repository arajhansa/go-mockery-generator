package com.github.arajhansa.gomockerygenerator.util

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications.Bus
import java.util.*
import javax.swing.Icon

class NotificationsUtil {

    fun showError(message: String) = showMessage(message, NotificationType.ERROR)
    fun showWarn(message: String) = showMessage(message, NotificationType.WARNING)
    fun showInfo(message: String) = showMessage(message, NotificationType.INFORMATION)

    private fun showMessage(message: String, type: NotificationType) {
        val notification = Notification(UUID.randomUUID().toString(), null as Icon?, type)
        notification.setTitle("Go mockery generator")
        notification.isImportant = true
        notification.setContent(message)
        Bus.notify(notification)
    }

}