package com.example.chatvox.widget

import android.content.Context
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import com.example.chatvox.R


class ChatvoxWidget : GlanceAppWidget() {

    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val MEDIUM_SQUARE = DpSize(175.dp, 175.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        sizes = setOf(SMALL_SQUARE, MEDIUM_SQUARE, BIG_SQUARE)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            val size = when(LocalSize.current) {
                SMALL_SQUARE -> 100.dp
                MEDIUM_SQUARE -> 175.dp
                BIG_SQUARE -> 250.dp
                else -> 100.dp
            }

            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .size(size)
                        .background(colorProvider = GlanceTheme.colors.surface)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        provider = ImageProvider(R.drawable.ic_zundamon),
                        contentDescription = "Zundamon",
                        modifier = GlanceModifier
                            .cornerRadius(size)
                            .background(colorProvider = GlanceTheme.colors.surfaceVariant)
                    )
                }
            }
        }
    }
}