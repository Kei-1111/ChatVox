package com.example.chatvox.data

import com.example.chatvox.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    val appSettings: Flow<AppSettings>

    suspend fun updateAppSettings(appSettings: AppSettings)
}