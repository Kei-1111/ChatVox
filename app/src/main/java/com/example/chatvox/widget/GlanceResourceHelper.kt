package com.example.chatvox.widget

import android.content.Context
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext

@Composable
@ReadOnlyComposable
fun glanceStringResource(@StringRes id: Int): String {
    return LocalContext.current.getString(id)
}

@Composable
@ReadOnlyComposable
fun glanceDimensionResource(@DimenRes id: Int): Dp {
    val context: Context = LocalContext.current
    val pixels = context.resources.getDimension(id)
    return (pixels / context.resources.displayMetrics.density).dp
}