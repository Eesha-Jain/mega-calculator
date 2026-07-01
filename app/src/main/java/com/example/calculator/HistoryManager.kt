package com.example.calculator

import android.content.Context
import android.content.SharedPreferences

class HistoryManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("history_prefs", Context.MODE_PRIVATE)

    fun incrementFrequency(expression: String) {
        val currentCount = prefs.getInt(expression, 0)
        prefs.edit().putInt(expression, currentCount + 1).apply()
    }

    fun getFrequency(expression: String): Int {
        return prefs.getInt(expression, 0)
    }

    fun getFrequentOperations(threshold: Int): List<String> {
        val allEntries = prefs.all
        return allEntries.filter { it.value is Int && (it.value as Int) >= threshold }
            .keys.toList()
    }
    
    fun clearFrequency(expression: String) {
        prefs.edit().remove(expression).apply()
    }
}
