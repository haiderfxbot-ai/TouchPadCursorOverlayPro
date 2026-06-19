package com.touchpad.cursoroverlay

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TouchPadApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
