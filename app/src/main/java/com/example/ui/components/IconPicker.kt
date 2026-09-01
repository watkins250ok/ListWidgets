package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class PresetIcon(val key: String, val label: String, val icon: ImageVector)

val PRESET_LIST_ICONS = listOf(
    PresetIcon("checklist", "Checklist", Icons.Default.Checklist),
    PresetIcon("idea", "Idea", Icons.Default.Lightbulb),
    PresetIcon("note", "Note", Icons.Default.Description),
    PresetIcon("tag", "Tag", Icons.Default.Tag),
    PresetIcon("work", "Work", Icons.Default.Work),
    PresetIcon("shopping", "Shopping", Icons.Default.ShoppingCart),
    PresetIcon("star", "Star", Icons.Default.Star),
    PresetIcon("bookmark", "Bookmark", Icons.Default.Bookmark),
    PresetIcon("heart", "Favorite", Icons.Default.Favorite),
    PresetIcon("folder", "Folder", Icons.Default.Folder),
    PresetIcon("sparkles", "Inspiration", Icons.Default.AutoAwesome),
    PresetIcon("code", "Code", Icons.Default.Code),
    PresetIcon("fitness", "Fitness", Icons.Default.FitnessCenter),
    PresetIcon("book", "Reading", Icons.Default.MenuBook),
    PresetIcon("home", "Home", Icons.Default.Home),
    PresetIcon("flag", "Important", Icons.Default.Flag),
    PresetIcon("school", "Study", Icons.Default.School),
    PresetIcon("music", "Music", Icons.Default.MusicNote),
    PresetIcon("palette", "Design", Icons.Default.Palette),
    PresetIcon("person", "Personal", Icons.Default.Person),
    PresetIcon("pin", "Pinned", Icons.Default.PushPin),
    PresetIcon("cafe", "Coffee", Icons.Default.LocalCafe),
    PresetIcon("car", "Travel", Icons.Default.DirectionsCar)
)

fun getVectorForIconName(iconName: String): ImageVector {
    return PRESET_LIST_ICONS.find { it.key.equals(iconName, ignoreCase = true) }?.icon ?: when (iconName.lowercase()) {
        "idea", "lightbulb" -> Icons.Default.Lightbulb
        "note", "description" -> Icons.Default.Description
        "checklist", "task" -> Icons.Default.Checklist
        else -> Icons.Default.Tag
    }
}

fun getVectorForCategory(categoryName: String, iconKey: String? = null): ImageVector {
    if (!iconKey.isNullOrBlank()) {
        val found = PRESET_LIST_ICONS.find { it.key.equals(iconKey, ignoreCase = true) }
        if (found != null) return found.icon
    }
    return when (categoryName.trim().lowercase()) {
        "idea", "ideas" -> Icons.Default.Lightbulb
        "task", "tasks" -> Icons.Default.Checklist
        "note", "notes" -> Icons.Default.Description
        "work" -> Icons.Default.Work
        "shopping", "shop" -> Icons.Default.ShoppingCart
        "fitness", "gym", "workout" -> Icons.Default.FitnessCenter
        "reading", "book", "books" -> Icons.Default.MenuBook
        "code", "coding", "dev" -> Icons.Default.Code
        "personal" -> Icons.Default.Person
        "home" -> Icons.Default.Home
        "study", "school" -> Icons.Default.School
        "design", "art" -> Icons.Default.Palette
        else -> Icons.Default.Tag
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IconPickerGrid(
    selectedIconName: String,
    onSelectIcon: (String) -> Unit,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PRESET_LIST_ICONS.forEach { preset ->
            val isSelected = preset.key.equals(selectedIconName, ignoreCase = true)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) accentColor.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelectIcon(preset.key) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = preset.icon,
                    contentDescription = preset.label,
                    tint = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
