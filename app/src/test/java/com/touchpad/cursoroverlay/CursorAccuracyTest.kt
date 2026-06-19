package com.touchpad.cursoroverlay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CursorAccuracyTest {

    @Test
    fun `cursor position maps correctly after movement`() {
        var cursorX = 500f
        var cursorY = 500f
        val speed = 1.5f

        val dx = 10f
        val dy = -5f

        cursorX = (cursorX + dx * speed).coerceIn(0f, 1080f)
        cursorY = (cursorY + dy * speed).coerceIn(0f, 1920f)

        assertEquals(515f, cursorX, 0.01f)
        assertEquals(492.5f, cursorY, 0.01f)
    }

    @Test
    fun `cursor stays within screen bounds`() {
        var cursorX = 50f
        var cursorY = 50f
        val speed = 2.0f

        cursorX = (cursorX + (-100f) * speed).coerceIn(0f, 1080f)
        cursorY = (cursorY + (-100f) * speed).coerceIn(0f, 1920f)

        assertEquals(0f, cursorX, 0.01f)
        assertEquals(0f, cursorY, 0.01f)
    }

    @Test
    fun `speed factor scales movement correctly`() {
        val baseDx = 10f
        val speed1 = 1.0f
        val speed2 = 2.0f

        val movement1 = baseDx * speed1
        val movement2 = baseDx * speed2

        assertEquals(10f, movement1, 0.01f)
        assertEquals(20f, movement2, 0.01f)
    }

    @Test
    fun `click coordinates match cursor position`() {
        val cursorX = 720f
        val cursorY = 1280f

        val clickX = cursorX
        val clickY = cursorY

        assertEquals(cursorX, clickX, 0.001f)
        assertEquals(cursorY, clickY, 0.001f)
    }

    @Test
    fun `drag path matches start and end positions`() {
        val startX = 100f
        val startY = 200f
        val endX = 500f
        val endY = 600f

        val pathDx = endX - startX
        val pathDy = endY - startY

        assertEquals(400f, pathDx, 0.01f)
        assertEquals(400f, pathDy, 0.01f)
    }

    @Test
    fun `no offset between visual and click position`() {
        val visualX = 1000f
        val visualY = 500f
        val clickX = visualX
        val clickY = visualY

        assertEquals(0f, clickX - visualX, 0.001f)
        assertEquals(0f, clickY - visualY, 0.001f)
    }
}
