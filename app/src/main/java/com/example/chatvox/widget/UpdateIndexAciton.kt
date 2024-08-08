package com.example.chatvox.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import dagger.hilt.android.EntryPointAccessors

class UpdateIndexAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val appPreferencesRepository = EntryPointAccessors.fromApplication(
            context,
            AppPreferencesRepositoryEntryPoint::class.java
        ).appPreferencesRepository()

        val index =
            parameters.getOrDefault(ActionParameters.Key(WIDGET_CURRENT_VOICEVOX_INDEX), 0)

        appPreferencesRepository.updateWidgetCurrentVoicevoxIndex(index)
    }

    companion object {
        const val WIDGET_CURRENT_VOICEVOX_INDEX = "widget_current_voicevox_index"
    }
}