package com.touchpad.cursoroverlay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.touchpad.cursoroverlay.data.SettingsRepository
import com.touchpad.cursoroverlay.data.models.OverlaySettings
import com.touchpad.cursoroverlay.util.PermissionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application) {

    val settings: StateFlow<OverlaySettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OverlaySettings())

    val onboardingCompleted: StateFlow<Boolean> = settingsRepository.onboardingCompleted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val allPermissionsGranted: Boolean
        get() = PermissionHelper.areAllPermissionsGranted(getApplication())

    fun completeOnboarding() {
        viewModelScope.launch {
            settingsRepository.completeOnboarding()
        }
    }

    fun startOverlay() {
        val context = getApplication<Application>()
        OverlayServiceController.start(context)
    }

    fun stopOverlay() {
        val context = getApplication<Application>()
        OverlayServiceController.stop(context)
    }
}
