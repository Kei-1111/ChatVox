package com.example.chatvox.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.chatvox.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppPreferencesRepository {

    companion object {
        private val IS_DYNAMIC_COLOR = booleanPreferencesKey("is_dynamic_color")
        private val IS_FOLLOW_SYSTEM_THEME = booleanPreferencesKey("is_follow_system_theme")
        private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val IS_NOT_LOGGED_IN = booleanPreferencesKey("is_not_logged_in")
        private val WIDGET_CURRENT_VOICEVOX_INDEX = intPreferencesKey("widget_current_voicevox_index")
    }

    override val appSettings: Flow<AppSettings> = dataStore.data
        .catch {
            if ( it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            AppSettings(
                isDynamicColor = preferences[IS_DYNAMIC_COLOR] ?: false,
                isFollowSystemTheme = preferences[IS_FOLLOW_SYSTEM_THEME] ?: true,
                isDarkTheme = preferences[IS_DARK_THEME] ?: false,
                userName = preferences[USER_NAME] ?: "",
                isNotLoggedIn = preferences[IS_NOT_LOGGED_IN] ?: false
            )
        }

    override val widgetCurrentVoicevoxIndex: Flow<Int> = dataStore.data
        .catch {
            if ( it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[WIDGET_CURRENT_VOICEVOX_INDEX] ?: 0
        }

    override suspend fun updateAppSettings(appSettings: AppSettings) {
        dataStore.edit { preferences ->
            preferences[IS_DYNAMIC_COLOR] = appSettings.isDynamicColor
            preferences[IS_FOLLOW_SYSTEM_THEME] = appSettings.isFollowSystemTheme
            preferences[IS_DARK_THEME] = appSettings.isDarkTheme
            preferences[USER_NAME] = appSettings.userName
            preferences[IS_NOT_LOGGED_IN] = appSettings.isNotLoggedIn
        }
    }

    override suspend fun updateWidgetCurrentVoicevoxIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[WIDGET_CURRENT_VOICEVOX_INDEX] = index
        }
    }
}