package com.github.arajhansa.gomockerygenerator.exception

import com.github.arajhansa.gomockerygenerator.util.NotificationsUtil

class NotifyException(override val message: String) : RuntimeException() {
    init {
        NotificationsUtil().showError(message)
    }
}

class NotifyInfo(override val message: String) : RuntimeException() {
    init {
        NotificationsUtil().showInfo(message)
    }
}
