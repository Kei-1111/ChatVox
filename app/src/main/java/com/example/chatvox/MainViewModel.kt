package com.example.chatvox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatvox.data.AppPreferencesRepository
import com.example.chatvox.model.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
) : ViewModel() {

    val appSettings = appPreferencesRepository.appSettings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = AppSettings(
            isLoading = true
        )
    )

    fun setUserName(userName: String) {
        viewModelScope.launch {
            appPreferencesRepository.updateAppSettings(
                appSettings.value.copy(
                    userName = userName,
                )
            )
        }
    }
}