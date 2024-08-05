package com.example.chatvox.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class ChatvoxWidgetReceiver : GlanceAppWidgetReceiver() {
    // 実際に表示するUIを定義したGlanceAppWidgetを返す
    override val glanceAppWidget: GlanceAppWidget = ChatvoxWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // 今回はstatelessなため画面更新は不要、特別な対応なし
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}