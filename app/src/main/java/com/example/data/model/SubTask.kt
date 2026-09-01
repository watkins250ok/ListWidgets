package com.example.data.model

import org.json.JSONArray
import org.json.JSONObject

data class SubTask(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false
) {
    companion object {
        fun fromJson(jsonStr: String): List<SubTask> {
            if (jsonStr.isBlank()) return emptyList()
            val list = mutableListOf<SubTask>()
            try {
                val array = JSONArray(jsonStr)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        SubTask(
                            id = obj.optString("id", System.currentTimeMillis().toString() + "_" + i),
                            title = obj.optString("title", ""),
                            isCompleted = obj.optBoolean("isCompleted", false)
                        )
                    )
                }
            } catch (_: Exception) {
            }
            return list
        }

        fun toJson(subtasks: List<SubTask>): String {
            val array = JSONArray()
            for (st in subtasks) {
                val obj = JSONObject()
                obj.put("id", st.id)
                obj.put("title", st.title)
                obj.put("isCompleted", st.isCompleted)
                array.put(obj)
            }
            return array.toString()
        }
    }
}
