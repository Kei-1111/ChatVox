package com.example.chatvox.widget

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import dagger.hilt.android.EntryPointAccessors

class ChatvoxWidget : GlanceAppWidget() {

    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val MEDIUM_SQUARE = DpSize(175.dp, 175.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        sizes = setOf(SMALL_SQUARE, MEDIUM_SQUARE, BIG_SQUARE)
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            val size = when (LocalSize.current) {
                SMALL_SQUARE -> 100.dp
                MEDIUM_SQUARE -> 175.dp
                BIG_SQUARE -> 250.dp
                else -> 100.dp
            }

            val appPreferencesRepository = EntryPointAccessors.fromApplication(
                context,
                AppPreferencesRepositoryEntryPoint::class.java
            ).appPreferencesRepository()

            ChatvoxWidgetContent(
                size = size,
                appPreferencesRepository = appPreferencesRepository,
            )
        }
    }
}