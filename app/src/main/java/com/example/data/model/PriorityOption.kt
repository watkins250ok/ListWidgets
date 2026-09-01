package com.example.data.model

data class PriorityOption(
    val id: String,
    val label: String,
    val colorHex: String
) {
    companion object {
        val DEFAULT_PRIORITIES = listOf(
            PriorityOption("NONE", "None", "#94A3B8"),
            PriorityOption("LOW", "Low", "#3B82F6"),
            PriorityOption("MEDIUM", "Medium", "#F59E0B"),
            PriorityOption("HIGH", "High", "#EF4444")
        )
    }
}
