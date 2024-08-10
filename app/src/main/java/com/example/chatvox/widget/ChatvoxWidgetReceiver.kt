package com.example.chatvox.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class ChatvoxWidgetReceiver : GlanceAppWidgetReceiver() {

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val chatvoxWidget by lazy {
        ChatvoxWidget(coroutineScope)
    }

    override val glanceAppWidget: GlanceAppWidget
        get() = chatvoxWidget
}