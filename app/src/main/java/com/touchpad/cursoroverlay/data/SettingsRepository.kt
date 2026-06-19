package com.touchpad.cursoroverlay.data

import com.touchpad.cursoroverlay.data.models.OverlaySettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    val settings: Flow<OverlaySettings> = preferencesManager.settingsFlow
    val onboardingCompleted: Flow<Boolean> = preferencesManager.onboardingCompletedFlow

    suspend fun updateOverlayPosition(x: Int, y: Int) =
        preferencesManager.updateOverlayPosition(x, y)

    suspend fun updateOverlaySize(width: Int, height: Int) =
        preferencesManager.updateOverlaySize(width, height)

    suspend fun updateOverlayOpacity(opacity: Float) =
        preferencesManager.updateOverlayOpacity(opacity)

    suspend fun updateCursorSpeed(speed: Float) =
        preferencesManager.updateCursorSpeed(speed)

    suspend fun updateCursorSize(size: Int) =
        preferencesManager.updateCursorSize(size)

    suspend fun updateCursorOpacity(opacity: Float) =
        preferencesManager.updateCursorOpacity(opacity)

    suspend fun updateDarkMode(enabled: Boolean) =
        preferencesManager.updateDarkMode(enabled)

    suspend fun updateAutoStart(enabled: Boolean) =
        preferencesManager.updateAutoStart(enabled)

    suspend fun updateAutoRestore(enabled: Boolean) =
        preferencesManager.updateAutoRestore(enabled)

    suspend fun updateVibrationFeedback(enabled: Boolean) =
        preferencesManager.updateVibrationFeedback(enabled)

    suspend fun updateSoundFeedback(enabled: Boolean) =
        preferencesManager.updateSoundFeedback(enabled)

    suspend fun completeOnboarding() =
        preferencesManager.completeOnboarding()

    suspend fun updateAll(settings: OverlaySettings) =
        preferencesManager.updateAll(settings)
}
