package com.example.chatvox.widget

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import com.example.chatvox.data.VoicevoxDataStore
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChatvoxWidget constructor(
    private val coroutineScope: CoroutineScope
) : GlanceAppWidget() {

    companion object {
        val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        val LARGE_SQUARE = DpSize(175.dp, 175.dp)

        private val WIDGET_CURRENT_VOICEVOX_INDEX =
            intPreferencesKey("widget_current_voicevox_index")
    }

    override val sizeMode = SizeMode.Responsive(
        sizes = setOf(SMALL_SQUARE, LARGE_SQUARE)
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            val appPreferencesRepository = EntryPointAccessors.fromApplication(
                context,
                WidgetEntryPoint::class.java
            ).appPreferencesRepository()

            var isDynamicColor by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    appPreferencesRepository.appSettings.collect { appSettings ->
                        isDynamicColor = appSettings.isDynamicColor
                    }
                }
            }

            val preferences: Preferences = currentState()
            val index = preferences[WIDGET_CURRENT_VOICEVOX_INDEX] ?: 0

            ChatvoxWidgetContent(
                isDynamicColor = isDynamicColor,
                index = index,
                onIncrease = {
                    coroutineScope.launch { increase(context, id) }
                },
                onDecrease = {
                    coroutineScope.launch { decrease(context, id) }
                }
            )
        }
    }

    private suspend fun increase(context: Context, glanceId: GlanceId) {
        updateAppWidgetState(context, glanceId) {
            val current = it[WIDGET_CURRENT_VOICEVOX_INDEX] ?: 0
            val next = (current + 1).let { if (it > VoicevoxDataStore.list.size - 1) 0 else it }
            it[WIDGET_CURRENT_VOICEVOX_INDEX] = next
        }
        update(context, glanceId)
    }

    private suspend fun decrease(context: Context, glanceId: GlanceId) {
        updateAppWidgetState(context, glanceId) {
            val current = it[WIDGET_CURRENT_VOICEVOX_INDEX] ?: 0
            val previous = (current - 1).let { if (it < 0) VoicevoxDataStore.list.size - 1 else it }
            it[WIDGET_CURRENT_VOICEVOX_INDEX] = previous
        }
        update(context, glanceId)
    }
}