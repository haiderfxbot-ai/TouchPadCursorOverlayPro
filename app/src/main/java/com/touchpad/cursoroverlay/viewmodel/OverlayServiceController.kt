package com.touchpad.cursoroverlay.viewmodel

import android.content.Context
import android.content.Intent
import com.touchpad.cursoroverlay.overlay.FloatingOverlayService

object OverlayServiceController {
    fun start(context: Context) {
        val intent = Intent(context, FloatingOverlayService::class.java)
        context.startForegroundService(intent)
    }

    fun stop(context: Context) {
        val intent = Intent(context, FloatingOverlayService::class.java)
        context.stopService(intent)
    }
}
