package com.example.chatvox.widget

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import dagger.hilt.android.EntryPointAccessors

class ChatvoxWidget : GlanceAppWidget() {

    companion object {
        val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        val LARGE_SQUARE = DpSize(175.dp, 175.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        sizes = setOf(SMALL_SQUARE, LARGE_SQUARE)
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            val appPreferencesRepository = EntryPointAccessors.fromApplication(
                context,
                AppPreferencesRepositoryEntryPoint::class.java
            ).appPreferencesRepository()

            ChatvoxWidgetContent(
                appPreferencesRepository = appPreferencesRepository,
            )
        }
    }
}