package com.example.chatvox.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatvox.data.AppPreferencesRepository
import com.example.chatvox.data.GeminiRepository
import com.example.chatvox.model.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    val uiState = appPreferencesRepository.appSettings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AppSettings()
    )

    fun onDynamicColorCheckedChange(isChecked: Boolean) {
        viewModelScope.launch {
            appPreferencesRepository.updateAppSettings(
                uiState.value.copy(isDynamicColor = isChecked)
            )
        }
    }

    fun onFollowSystemThemeCheckedChange(isChecked: Boolean) {
        viewModelScope.launch {
            appPreferencesRepository.updateAppSettings(
                uiState.value.copy(isFollowSystemTheme = isChecked)
            )
        }
    }

    fun onDarkThemeCheckedChange(isChecked: Boolean) {
        viewModelScope.launch {
            appPreferencesRepository.updateAppSettings(
                uiState.value.copy(isDarkTheme = isChecked)
            )
        }
    }

    fun setUserName(userName: String) {
        viewModelScope.launch {
            appPreferencesRepository.updateAppSettings(
                uiState.value.copy(userName = userName)
            )
            geminiRepository.resetVoicevox()
        }
    }
}