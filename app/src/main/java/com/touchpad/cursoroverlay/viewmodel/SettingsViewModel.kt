package com.touchpad.cursoroverlay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.touchpad.cursoroverlay.data.SettingsRepository
import com.touchpad.cursoroverlay.data.models.OverlaySettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application) {

    val settings: StateFlow<OverlaySettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OverlaySettings())

    fun updateOverlayPosition(x: Int, y: Int) {
        viewModelScope.launch { settingsRepository.updateOverlayPosition(x, y) }
    }

    fun updateOverlaySize(width: Int, height: Int) {
        viewModelScope.launch { settingsRepository.updateOverlaySize(width, height) }
    }

    fun updateOverlayOpacity(opacity: Float) {
        viewModelScope.launch { settingsRepository.updateOverlayOpacity(opacity) }
    }

    fun updateCursorSpeed(speed: Float) {
        viewModelScope.launch { settingsRepository.updateCursorSpeed(speed) }
    }

    fun updateCursorSize(size: Int) {
        viewModelScope.launch { settingsRepository.updateCursorSize(size) }
    }

    fun updateCursorOpacity(opacity: Float) {
        viewModelScope.launch { settingsRepository.updateCursorOpacity(opacity) }
    }

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateDarkMode(enabled) }
    }

    fun updateAutoStart(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateAutoStart(enabled) }
    }

    fun updateAutoRestore(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateAutoRestore(enabled) }
    }

    fun updateVibrationFeedback(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateVibrationFeedback(enabled) }
    }

    fun updateSoundFeedback(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateSoundFeedback(enabled) }
    }
}
