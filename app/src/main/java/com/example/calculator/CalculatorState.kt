package com.example.calculator

import android.content.Context

object CalculatorState {
    const val WIDGET_PREFS = "widget_calculator_prefs"
    const val VARS_PREFS = "user_vars_prefs"
    const val SHORTCUTS_PREFS = "user_shortcuts_prefs"
    const val SETTINGS_PREFS = "user_settings_prefs"

    const val PREF_EXPRESSION = "expression"
    const val PREF_DISPLAY = "display"
    const val PREF_SHOULD_REPLACE = "should_replace"
    
    const val GESTURE_TYPING_KEY = "gesture_typing_enabled"
    const val SHORTCUT_THRESHOLD_KEY = "shortcut_threshold"

    fun getExpression(context: Context): String =
        context.getSharedPreferences(WIDGET_PREFS, Context.MODE_PRIVATE)
            .getString(PREF_EXPRESSION, "") ?: ""

    fun setWidgetState(context: Context, expression: String, display: String, shouldReplace: Boolean) {
        context.getSharedPreferences(WIDGET_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_EXPRESSION, expression)
            .putString(PREF_DISPLAY, display)
            .putBoolean(PREF_SHOULD_REPLACE, shouldReplace)
            .apply()
    }
    
    fun loadAllVariables(context: Context): MutableMap<String, Double?> {
        val prefs = context.getSharedPreferences(VARS_PREFS, Context.MODE_PRIVATE)
        val map = mutableMapOf<String, Double?>()

        listOf("v1", "v2", "v3").forEach { name ->
            map[name] = prefs.getString(name, null)?.toDoubleOrNull()
        }

        prefs.all.forEach { (key, value) ->
            if (key != "ans" && key !in map) {
                val d = value.toString().toDoubleOrNull()
                map[key] = d
            }
        }

        map["ans"] = prefs.getString("ans", null)?.toDoubleOrNull()
        return map
    }

    fun loadShortcuts(context: Context): MutableMap<String, String> {
        val prefs = context.getSharedPreferences(SHORTCUTS_PREFS, Context.MODE_PRIVATE)
        val map = mutableMapOf<String, String>()

        map["s1"] = prefs.getString("s1", "") ?: ""

        prefs.all.forEach { (key, value) ->
            if (value is String && key !in map) map[key] = value
        }
        return map
    }
}
