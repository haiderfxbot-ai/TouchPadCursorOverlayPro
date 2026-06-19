package com.touchpad.cursoroverlay.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import java.util.concurrent.Executors

class TouchPadAccessibilityService : AccessibilityService() {

    private val gestureExecutor = Executors.newSingleThreadExecutor()
    private var isDragging = false
    private var dragStartX = 0f
    private var dragStartY = 0f

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onKeyEvent(event: KeyEvent): Boolean {
        return super.onKeyEvent(event)
    }

    fun performLeftClick(x: Float, y: Float) {
        dispatchClick(x, y)
    }

    fun performDoubleClick(x: Float, y: Float) {
        val path1 = Path().apply { moveTo(x, y) }
        val path2 = Path().apply { moveTo(x, y) }

        val gesture1 = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path1, 0, 1))
            .build()

        val gesture2 = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path2, 100, 1))
            .build()

        dispatchGesture(gesture1, null, null)
        dispatchGesture(gesture2, null, null)
    }

    fun performLongClick(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 500))
            .build()
        dispatchGesture(gesture, null, null)
    }

    fun performRightClick(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
            .build()
        dispatchGesture(gesture, null, null)
    }

    fun startDrag(x: Float, y: Float) {
        isDragging = true
        dragStartX = x
        dragStartY = y
    }

    fun performDragTo(x: Float, y: Float) {
        if (!isDragging) return
        val path = Path().apply {
            moveTo(dragStartX, dragStartY)
            lineTo(x, y)
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 50))
            .build()
        dispatchGesture(gesture, null, null)
        dragStartX = x
        dragStartY = y
    }

    fun stopDrag() {
        isDragging = false
    }

    fun performSwipe(x1: Float, y1: Float, x2: Float, y2: Float, duration: Long = 100) {
        val path = Path().apply {
            moveTo(x1, y1)
            lineTo(x2, y2)
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()
        dispatchGesture(gesture, null, null)
    }

    private fun dispatchClick(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val clickDuration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) 1L else 50L
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, clickDuration))
            .build()
        dispatchGesture(gesture, null, null)
    }

    fun injectJavaScript(jsCode: String) {
        val path = Path().apply { moveTo(0f, 0f) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
            .build()
        dispatchGesture(gesture, null, null)
    }

    override fun onDestroy() {
        gestureExecutor.shutdown()
        super.onDestroy()
    }

    companion object {
        var instance: TouchPadAccessibilityService? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
