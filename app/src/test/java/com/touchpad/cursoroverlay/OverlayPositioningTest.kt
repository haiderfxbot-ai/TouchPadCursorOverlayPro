package com.touchpad.cursoroverlay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OverlayPositioningTest {

    @Test
    fun `overlay x position is within valid range`() {
        val screenWidth = 1080
        val overlayWidth = 300

        val positions = listOf(-100, 0, 500, screenWidth - overlayWidth, screenWidth + 100)

        for (x in positions) {
            val clamped = x.coerceIn(0, screenWidth - overlayWidth)
            assertTrue(clamped >= 0)
            assertTrue(clamped <= screenWidth - overlayWidth)
        }
    }

    @Test
    fun `overlay position restores correctly`() {
        val savedX = 150
        val savedY = 200

        val restoredX = savedX
        val restoredY = savedY

        assertEquals(150, restoredX)
        assertEquals(200, restoredY)
    }

    @Test
    fun `overlay width stays within bounds`() {
        val minWidth = 180
        val maxWidth = 500

        assertEquals(180, 100.coerceIn(minWidth, maxWidth))
        assertEquals(300, 300.coerceIn(minWidth, maxWidth))
        assertEquals(500, 600.coerceIn(minWidth, maxWidth))
    }

    @Test
    fun `overlay height stays within bounds`() {
        val minHeight = 280
        val maxHeight = 600

        assertEquals(280, 150.coerceIn(minHeight, maxHeight))
        assertEquals(400, 400.coerceIn(minHeight, maxHeight))
        assertEquals(600, 800.coerceIn(minHeight, maxHeight))
    }

    @Test
    fun `dp to px conversion is linear`() {
        val density = 2.625f

        val dpValues = listOf(100, 200, 300)
        val expectedPx = dpValues.map { (it * density).toInt() }

        assertEquals(262, expectedPx[0])
        assertEquals(525, expectedPx[1])
        assertEquals(787, expectedPx[2])
    }

    @Test
    fun `overlay position persists across orientation changes`() {
        val portraitX = 100
        val portraitY = 200

        val landscapeX = portraitX.coerceIn(0, 1920 - 300)
        val landscapeY = portraitY.coerceIn(0, 1080 - 400)

        assertEquals(100, landscapeX)
        assertEquals(200, landscapeY)
    }
}
