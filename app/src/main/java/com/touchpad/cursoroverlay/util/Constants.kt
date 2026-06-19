package com.touchpad.cursoroverlay.util

object Constants {
    const val NOTIFICATION_CHANNEL_ID = "touchpad_overlay_channel"
    const val NOTIFICATION_CHANNEL_NAME = "TouchPad Overlay"
    const val NOTIFICATION_ID = 1001
    const val FOREGROUND_SERVICE_ID = 1002

    const val MIN_OVERLAY_WIDTH = 180
    const val MAX_OVERLAY_WIDTH = 500
    const val MIN_OVERLAY_HEIGHT = 280
    const val MAX_OVERLAY_HEIGHT = 600

    const val MIN_CURSOR_SIZE = 10
    const val MAX_CURSOR_SIZE = 50

    const val MIN_CURSOR_SPEED = 0.3f
    const val MAX_CURSOR_SPEED = 3.0f

    const val DOUBLE_CLICK_INTERVAL_MS = 300L
    const val LONG_PRESS_DURATION_MS = 500L

    const val ACTION_START_OVERLAY = "com.touchpad.cursoroverlay.ACTION_START"
    const val ACTION_STOP_OVERLAY = "com.touchpad.cursoroverlay.ACTION_STOP"
    const val ACTION_UPDATE_SETTINGS = "com.touchpad.cursoroverlay.ACTION_UPDATE"

    const val PREF_DARK_MODE = "pref_dark_mode"
}
