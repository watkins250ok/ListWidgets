package com.example.widget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.MainActivity
import com.example.data.repository.ListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WidgetActionActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        val toggleCheck = intent.getBooleanExtra(ListAppWidgetProvider.EXTRA_TOGGLE_CHECK, false)
        val toggleSubtask = intent.getBooleanExtra(ListAppWidgetProvider.EXTRA_TOGGLE_SUBTASK, false)
        val itemId = intent.getLongExtra(ListAppWidgetProvider.EXTRA_ITEM_ID, -1L)
        val isChecked = intent.getBooleanExtra(ListAppWidgetProvider.EXTRA_IS_CHECKED, false)
        val subtaskIndex = intent.getIntExtra(ListAppWidgetProvider.EXTRA_SUBTASK_INDEX, -1)

        if (toggleCheck && itemId > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = ListRepository(applicationContext)
                    repo.toggleTaskChecked(itemId, !isChecked)
                } catch (_: Throwable) {
                } finally {
                    finish()
                    overridePendingTransition(0, 0)
                }
            }
        } else if (toggleSubtask && itemId > 0 && subtaskIndex >= 0) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = ListRepository(applicationContext)
                    repo.toggleSubTaskChecked(itemId, subtaskIndex)
                } catch (_: Throwable) {
                } finally {
                    finish()
                    overridePendingTransition(0, 0)
                }
            }
        } else {
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
}
