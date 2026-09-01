package com.example.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.model.ListEntity
import com.example.data.model.ListItemEntity
import com.example.data.model.WidgetType
import com.example.data.repository.ListRepository
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ListWidgetRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
        val listId = intent.getLongExtra(ListAppWidgetProvider.EXTRA_LIST_ID, 0L)
        return ListWidgetRemoteViewsFactory(applicationContext, appWidgetId, listId)
    }
}

class ListWidgetRemoteViewsFactory(
    private val context: Context,
    private val appWidgetId: Int,
    private val listId: Long
) : RemoteViewsService.RemoteViewsFactory {

    private var items: List<ListItemEntity> = emptyList()
    private var lists: List<ListEntity> = emptyList()
    private var listCountMap: Map<Long, Int> = emptyMap()
    private var currentWidgetType: WidgetType = WidgetType.SPECIFIC_LIST
    private var showDescription: Boolean = true
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    private val dueDateFormat = SimpleDateFormat("MMM d", Locale.getDefault())

    override fun onCreate() {
        fetchData()
    }

    override fun onDataSetChanged() {
        fetchData()
    }

    private fun fetchData() {
        runBlocking {
            try {
                val db = AppDatabase.getDatabase(context)
                val repo = ListRepository(context)
                val config = repo.getWidgetConfigForAppWidgetId(appWidgetId)
                currentWidgetType = config.widgetType
                showDescription = config.showDescription

                if (config.widgetType == WidgetType.ALL_LISTS) {
                    val allLists = db.listDao().getAllListsSync()
                    val allItems = db.listItemDao().getAllItemsSync()
                    lists = allLists
                    listCountMap = allItems.groupingBy { it.listId }.eachCount()
                    items = emptyList()
                } else if (config.widgetType == WidgetType.ALL_ITEMS) {
                    var allItems = db.listItemDao().getAllItemsSync()
                    if (config.categoryFilter != "ALL") {
                        allItems = allItems.filter { it.category.equals(config.categoryFilter, ignoreCase = true) }
                    }
                    if (config.tagFilter != "ALL") {
                        val searchTag = config.tagFilter.removePrefix("#").trim()
                        allItems = allItems.filter { it.tags.contains(searchTag, ignoreCase = true) }
                    }
                    if (config.hideChecked) {
                        allItems = allItems.filter { !it.isChecked }
                    }
                    items = allItems
                    lists = emptyList()
                } else {
                    // SPECIFIC_LIST
                    val targetListId = if (config.listId > 0) config.listId else listId
                    val allItems = if (targetListId > 0) {
                        db.listItemDao().getItemsForListSync(targetListId)
                    } else {
                        db.listItemDao().getAllItemsSync()
                    }
                    items = if (config.hideChecked) {
                        allItems.filter { !it.isChecked }
                    } else {
                        allItems
                    }
                    lists = emptyList()
                }
            } catch (_: Throwable) {}
        }
    }

    override fun onDestroy() {
        items = emptyList()
        lists = emptyList()
    }

    override fun getCount(): Int {
        return if (currentWidgetType == WidgetType.ALL_LISTS) lists.size else items.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        if (currentWidgetType == WidgetType.ALL_LISTS) {
            if (position !in lists.indices) return RemoteViews(context.packageName, R.layout.widget_all_lists_row)
            val list = lists[position]
            val views = RemoteViews(context.packageName, R.layout.widget_all_lists_row)

            views.setTextViewText(R.id.widget_list_row_name, list.name)

            // Icon
            val iconEmoji = when (list.iconName) {
                "check", "checklist" -> "✅"
                "shopping" -> "🛒"
                "work" -> "💼"
                "idea" -> "💡"
                "star" -> "⭐"
                "folder" -> "📁"
                "book", "bookmark" -> "📖"
                "favorite", "heart" -> "❤️"
                "plane" -> "✈️"
                "fitness" -> "🏋️"
                else -> "📋"
            }
            views.setTextViewText(R.id.widget_list_row_icon, iconEmoji)

            // Description
            if (!list.description.isNullOrBlank()) {
                views.setViewVisibility(R.id.widget_list_row_desc, View.VISIBLE)
                views.setTextViewText(R.id.widget_list_row_desc, list.description)
            } else {
                views.setViewVisibility(R.id.widget_list_row_desc, View.GONE)
            }

            // Creation Date
            val createdStr = dateFormat.format(Date(list.createdAt))
            views.setTextViewText(R.id.widget_list_row_date, "📅 Created $createdStr")

            // Count badge
            val count = listCountMap[list.id] ?: 0
            views.setTextViewText(R.id.widget_list_row_count, "$count item${if (count == 1) "" else "s"}")

            val fillInIntent = Intent().apply {
                putExtra(ListAppWidgetProvider.EXTRA_LIST_ID, list.id)
            }
            views.setOnClickFillInIntent(R.id.widget_list_row_container, fillInIntent)
            return views
        }

        // Render Item Row for ALL_ITEMS and SPECIFIC_LIST
        if (position !in items.indices) return RemoteViews(context.packageName, R.layout.widget_item_row)
        val item = items[position]
        val views = RemoteViews(context.packageName, R.layout.widget_item_row)

        views.setTextViewText(R.id.widget_item_title, item.title)

        val isTask = item.type == "TASK"
        if (isTask) {
            if (item.isChecked) {
                views.setImageViewResource(R.id.widget_item_check_icon, R.drawable.ic_widget_checkbox_checked)
                views.setTextColor(R.id.widget_item_title, Color.parseColor("#94A3B8"))
            } else {
                views.setImageViewResource(R.id.widget_item_check_icon, R.drawable.ic_widget_checkbox_unchecked)
                views.setTextColor(R.id.widget_item_title, Color.parseColor("#FFFFFF"))
            }
        } else {
            views.setImageViewResource(R.id.widget_item_check_icon, R.drawable.ic_widget_bullet_dot)
            views.setTextColor(R.id.widget_item_title, Color.parseColor("#FFFFFF"))
        }

        // Due Date badge
        if (item.dueDate != null && item.dueDate > 0) {
            views.setViewVisibility(R.id.widget_item_due_badge, View.VISIBLE)
            val dueLabel = formatDueDateLabel(item.dueDate)
            views.setTextViewText(R.id.widget_item_due_badge, "📅 $dueLabel")
        } else {
            views.setViewVisibility(R.id.widget_item_due_badge, View.GONE)
        }

        // Tags
        if (item.tags.isNotBlank()) {
            val firstTag = item.tags.split(",").firstOrNull()?.trim() ?: ""
            if (firstTag.isNotBlank()) {
                views.setViewVisibility(R.id.widget_item_tag_badge, View.VISIBLE)
                views.setTextViewText(R.id.widget_item_tag_badge, "#$firstTag")
            } else {
                views.setViewVisibility(R.id.widget_item_tag_badge, View.GONE)
            }
        } else {
            views.setViewVisibility(R.id.widget_item_tag_badge, View.GONE)
        }

        // Description preview
        if (showDescription && item.description.isNotBlank()) {
            views.setViewVisibility(R.id.widget_item_desc, View.VISIBLE)
            val cleanDesc = item.description.replace("#", "").replace("*", "").replace(">", "").trim()
            views.setTextViewText(R.id.widget_item_desc, cleanDesc)
        } else {
            views.setViewVisibility(R.id.widget_item_desc, View.GONE)
        }

        // Fill-in Intent
        val fillInIntent = Intent().apply {
            putExtra(ListAppWidgetProvider.EXTRA_OPEN_ITEM_ID, item.id)
            putExtra(ListAppWidgetProvider.EXTRA_LIST_ID, item.listId)
            putExtra(ListAppWidgetProvider.EXTRA_IS_CHECKED, item.isChecked)
            putExtra("is_task", isTask)
        }
        views.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent)

        return views
    }

    private fun formatDueDateLabel(dueMillis: Long): String {
        val now = Calendar.getInstance()
        val due = Calendar.getInstance().apply { timeInMillis = dueMillis }

        return when {
            now.get(Calendar.YEAR) == due.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == due.get(Calendar.DAY_OF_YEAR) -> "DUE: TODAY"
            now.get(Calendar.YEAR) == due.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) + 1 == due.get(Calendar.DAY_OF_YEAR) -> "DUE: TOMORROW"
            else -> "DUE: " + dueDateFormat.format(Date(dueMillis)).uppercase()
        }
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 2
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = false
}

