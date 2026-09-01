package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconName: String = "checklist", // preset icon name e.g. "checklist", "idea", "work", "shopping", "star", "bookmark", "heart", "folder", "sparkles"
    val customIconUri: String? = null,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val colorHex: String = "#7C3AED", // Primary list accent color
    val autoDeleteMode: String = "NEVER", // "NEVER", "IMMEDIATELY", "AFTER_24H", "AFTER_1W"
    val hideCheckedItems: Boolean = false
)
