package com.touchpad.cursoroverlay

import com.touchpad.cursoroverlay.data.models.OverlaySettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsRepositoryTest {

    @Test
    fun `default settings have correct values`() {
        val settings = OverlaySettings()

        assertEquals(100, settings.overlayX)
        assertEquals(100, settings.overlayY)
        assertEquals(260, settings.overlayWidth)
        assertEquals(380, settings.overlayHeight)
        assertEquals(0.85f, settings.overlayOpacity)
        assertEquals(1.0f, settings.cursorSpeed)
        assertEquals(20, settings.cursorSize)
        assertEquals(1.0f, settings.cursorOpacity)
        assertTrue(settings.isDarkMode)
        assertFalse(settings.autoStart)
        assertTrue(settings.autoRestore)
        assertTrue(settings.vibrationFeedback)
        assertFalse(settings.soundFeedback)
    }

    @Test
    fun `settings can be fully updated`() {
        val original = OverlaySettings()
        val updated = original.copy(
            overlayX = 200,
            overlayY = 300,
            overlayWidth = 320,
            overlayHeight = 450,
            overlayOpacity = 0.9f,
            cursorSpeed = 1.5f,
            cursorSize = 30,
            cursorOpacity = 0.8f,
            isDarkMode = false,
            autoStart = true,
            autoRestore = false,
            vibrationFeedback = false,
            soundFeedback = true
        )

        assertEquals(200, updated.overlayX)
        assertEquals(300, updated.overlayY)
        assertEquals(320, updated.overlayWidth)
        assertEquals(450, updated.overlayHeight)
        assertEquals(0.9f, updated.overlayOpacity)
        assertEquals(1.5f, updated.cursorSpeed)
        assertEquals(30, updated.cursorSize)
        assertEquals(0.8f, updated.cursorOpacity)
        assertFalse(updated.isDarkMode)
        assertTrue(updated.autoStart)
        assertFalse(updated.autoRestore)
        assertFalse(updated.vibrationFeedback)
        assertTrue(updated.soundFeedback)
    }

    @Test
    fun `opacity is clamped to valid range`() {
        val settings = OverlaySettings(overlayOpacity = 1.5f)
        val clamped = settings.overlayOpacity.coerceIn(0.1f, 1.0f)

        assertEquals(1.0f, clamped, 0.01f)

        val settings2 = OverlaySettings(overlayOpacity = -0.5f)
        val clamped2 = settings2.overlayOpacity.coerceIn(0.1f, 1.0f)

        assertEquals(0.1f, clamped2, 0.01f)
    }

    @Test
    fun `cursor speed is clamped to valid range`() {
        assertEquals(0.3f, 0.1f.coerceIn(0.3f, 3.0f), 0.01f)
        assertEquals(1.5f, 1.5f.coerceIn(0.3f, 3.0f), 0.01f)
        assertEquals(3.0f, 5.0f.coerceIn(0.3f, 3.0f), 0.01f)
    }
}
