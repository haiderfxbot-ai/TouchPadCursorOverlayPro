package com.touchpad.cursoroverlay.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.touchpad.cursoroverlay.data.models.OverlaySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "touchpad_settings")

@Singleton
class PreferencesManager @Inject constructor(
    private val context: Context
) {
    private object Keys {
        val OVERLAY_X = intPreferencesKey("overlay_x")
        val OVERLAY_Y = intPreferencesKey("overlay_y")
        val OVERLAY_WIDTH = intPreferencesKey("overlay_width")
        val OVERLAY_HEIGHT = intPreferencesKey("overlay_height")
        val OVERLAY_OPACITY = floatPreferencesKey("overlay_opacity")
        val CURSOR_SPEED = floatPreferencesKey("cursor_speed")
        val CURSOR_SIZE = intPreferencesKey("cursor_size")
        val CURSOR_OPACITY = floatPreferencesKey("cursor_opacity")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val AUTO_START = booleanPreferencesKey("auto_start")
        val AUTO_RESTORE = booleanPreferencesKey("auto_restore")
        val VIBRATION_FEEDBACK = booleanPreferencesKey("vibration_feedback")
        val SOUND_FEEDBACK = booleanPreferencesKey("sound_feedback")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val settingsFlow: Flow<OverlaySettings> = context.dataStore.data.map { prefs ->
        OverlaySettings(
            overlayX = prefs[Keys.OVERLAY_X] ?: 100,
            overlayY = prefs[Keys.OVERLAY_Y] ?: 100,
            overlayWidth = prefs[Keys.OVERLAY_WIDTH] ?: 260,
            overlayHeight = prefs[Keys.OVERLAY_HEIGHT] ?: 380,
            overlayOpacity = prefs[Keys.OVERLAY_OPACITY] ?: 0.85f,
            cursorSpeed = prefs[Keys.CURSOR_SPEED] ?: 1.0f,
            cursorSize = prefs[Keys.CURSOR_SIZE] ?: 20,
            cursorOpacity = prefs[Keys.CURSOR_OPACITY] ?: 1.0f,
            isDarkMode = prefs[Keys.IS_DARK_MODE] ?: true,
            autoStart = prefs[Keys.AUTO_START] ?: false,
            autoRestore = prefs[Keys.AUTO_RESTORE] ?: true,
            vibrationFeedback = prefs[Keys.VIBRATION_FEEDBACK] ?: true,
            soundFeedback = prefs[Keys.SOUND_FEEDBACK] ?: false
        )
    }

    val onboardingCompletedFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETED] ?: false
    }

    suspend fun updateOverlayPosition(x: Int, y: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.OVERLAY_X] = x
            prefs[Keys.OVERLAY_Y] = y
        }
    }

    suspend fun updateOverlaySize(width: Int, height: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.OVERLAY_WIDTH] = width
            prefs[Keys.OVERLAY_HEIGHT] = height
        }
    }

    suspend fun updateOverlayOpacity(opacity: Float) {
        context.dataStore.edit { prefs ->
            prefs[Keys.OVERLAY_OPACITY] = opacity
        }
    }

    suspend fun updateCursorSpeed(speed: Float) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CURSOR_SPEED] = speed
        }
    }

    suspend fun updateCursorSize(size: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CURSOR_SIZE] = size
        }
    }

    suspend fun updateCursorOpacity(opacity: Float) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CURSOR_OPACITY] = opacity
        }
    }

    suspend fun updateDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_DARK_MODE] = enabled
        }
    }

    suspend fun updateAutoStart(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.AUTO_START] = enabled
        }
    }

    suspend fun updateAutoRestore(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.AUTO_RESTORE] = enabled
        }
    }

    suspend fun updateVibrationFeedback(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.VIBRATION_FEEDBACK] = enabled
        }
    }

    suspend fun updateSoundFeedback(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SOUND_FEEDBACK] = enabled
        }
    }

    suspend fun completeOnboarding() {
        context.dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun updateAll(settings: OverlaySettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.OVERLAY_X] = settings.overlayX
            prefs[Keys.OVERLAY_Y] = settings.overlayY
            prefs[Keys.OVERLAY_WIDTH] = settings.overlayWidth
            prefs[Keys.OVERLAY_HEIGHT] = settings.overlayHeight
            prefs[Keys.OVERLAY_OPACITY] = settings.overlayOpacity
            prefs[Keys.CURSOR_SPEED] = settings.cursorSpeed
            prefs[Keys.CURSOR_SIZE] = settings.cursorSize
            prefs[Keys.CURSOR_OPACITY] = settings.cursorOpacity
            prefs[Keys.IS_DARK_MODE] = settings.isDarkMode
            prefs[Keys.AUTO_START] = settings.autoStart
            prefs[Keys.AUTO_RESTORE] = settings.autoRestore
            prefs[Keys.VIBRATION_FEEDBACK] = settings.vibrationFeedback
            prefs[Keys.SOUND_FEEDBACK] = settings.soundFeedback
        }
    }
}
