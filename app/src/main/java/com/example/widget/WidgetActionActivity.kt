package com.example.widget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.MainActivity

class WidgetActionActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val openItemId = intent.getLongExtra(ListAppWidgetProvider.EXTRA_OPEN_ITEM_ID, -1L)
        val listId = intent.getLongExtra(ListAppWidgetProvider.EXTRA_LIST_ID, -1L)

        val targetIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (openItemId > 0) {
                putExtra(ListAppWidgetProvider.EXTRA_OPEN_ITEM_ID, openItemId)
            }
            if (listId > 0) {
                putExtra(ListAppWidgetProvider.EXTRA_LIST_ID, listId)
            }
        }
        startActivity(targetIntent)
        finish()
    }
}
