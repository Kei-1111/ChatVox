package com.example.chatvox.widget

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme
import androidx.glance.material3.ColorProviders
import com.example.chatvox.ui.theme.darkScheme
import com.example.chatvox.ui.theme.lightScheme

@Composable
fun WidgetTheme(
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    val colors =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            GlanceTheme.colors
        } else {
            ColorProviders(
                light = lightScheme,
                dark = darkScheme,
            )
        }

    GlanceTheme(colors) {
        content()
    }
}
