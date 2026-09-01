package com.example.widget

import android.appwidget.AppWidgetManager
import android.content.Context

class SpecificListWidgetProvider : ListAppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}
