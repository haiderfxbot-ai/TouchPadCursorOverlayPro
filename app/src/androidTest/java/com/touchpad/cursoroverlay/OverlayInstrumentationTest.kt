package com.touchpad.cursoroverlay

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.touchpad.cursoroverlay.util.PermissionHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverlayInstrumentationTest {

    @Test
    fun `application context is not null`() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(appContext)
        assertEquals("com.touchpad.cursoroverlay", appContext.packageName)
    }

    @Test
    fun `overlay permission check does not crash`() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        try {
            PermissionHelper.hasOverlayPermission(appContext)
        } catch (e: Exception) {
            throw AssertionError("Overlay permission check threw exception: ${e.message}")
        }
    }

    @Test
    fun `accessibility permission check does not crash`() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        try {
            PermissionHelper.hasAccessibilityPermission(appContext)
        } catch (e: Exception) {
            throw AssertionError("Accessibility permission check threw exception: ${e.message}")
        }
    }

    @Test
    fun `resources contains app name`() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appName = appContext.getString(R.string.app_name)
        assertEquals("TouchPad Cursor Overlay Pro", appName)
    }
}
