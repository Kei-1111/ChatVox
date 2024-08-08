package com.example.chatvox.widget

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.IconCompat
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GlanceIcon(
    modifier: GlanceModifier = GlanceModifier,
    iconRes: Int,
    contentDescription: String,
    color: androidx.compose.ui.graphics.Color,
) {
    Image(
        provider = ImageProvider(
            IconCompat
                .createWithResource(
                    LocalContext.current,
                    iconRes
                )
                .setTint(color.toArgb())
                .toIcon(LocalContext.current)
        ),
        contentDescription = contentDescription,
        modifier = modifier
    )
}