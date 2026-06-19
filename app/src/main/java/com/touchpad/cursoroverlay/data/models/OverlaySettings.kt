package com.touchpad.cursoroverlay.data.models

data class OverlaySettings(
    val overlayX: Int = 100,
    val overlayY: Int = 100,
    val overlayWidth: Int = 260,
    val overlayHeight: Int = 380,
    val overlayOpacity: Float = 0.85f,
    val cursorSpeed: Float = 1.0f,
    val cursorSize: Int = 20,
    val cursorOpacity: Float = 1.0f,
    val isDarkMode: Boolean = true,
    val autoStart: Boolean = false,
    val autoRestore: Boolean = true,
    val vibrationFeedback: Boolean = true,
    val soundFeedback: Boolean = false,
    val isOverlayActive: Boolean = false
)
