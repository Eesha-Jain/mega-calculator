package com.example.calculator

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class CalculatorWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_KEY_PRESS) {
            val key = intent.getStringExtra(EXTRA_KEY) ?: return
            var expression = CalculatorState.getExpression(context)
            var display = context.getSharedPreferences(CalculatorState.WIDGET_PREFS, Context.MODE_PRIVATE)
                .getString(CalculatorState.PREF_DISPLAY, "0") ?: "0"
            var shouldReplace = context.getSharedPreferences(CalculatorState.WIDGET_PREFS, Context.MODE_PRIVATE)
                .getBoolean(CalculatorState.PREF_SHOULD_REPLACE, false)
            
            when (key) {
                "C" -> {
                    expression = ""
                    display = "0"
                    shouldReplace = false
                }
                "=" -> {
                    val variables = CalculatorState.loadAllVariables(context)
                    val shortcuts = CalculatorState.loadShortcuts(context)
                    val result = CalculatorEngine.safeEvaluate(expression, variables, shortcuts)
                    if (result != "Error") {
                        context.getSharedPreferences(CalculatorState.VARS_PREFS, Context.MODE_PRIVATE)
                            .edit().putString("ans", result).apply()
                        display = result
                        shouldReplace = true
                    } else {
                        display = "Error"
                        shouldReplace = true
                    }
                }
                else -> {
                    val isDigit = key.firstOrNull()?.isDigit() == true || key == "."
                    if (shouldReplace && isDigit) {
                        expression = if (key == ".") "0." else key
                        display = expression
                        shouldReplace = false
                    } else {
                        if (shouldReplace) {
                            expression = display
                            shouldReplace = false
                        }
                        val visualOps = setOf('+', '−', '×', '÷', '^')
                        if (key.length == 1 && key[0] in visualOps) {
                            if (expression.isNotEmpty() && expression.last() in visualOps) {
                                expression = expression.dropLast(1)
                            }
                        }
                        if (expression == "" && key.firstOrNull()?.let { it in visualOps && it != '−' } == true) {
                            return@onReceive
                        }
                        expression += key
                        display = expression
                    }
                }
            }
            
            CalculatorState.setWidgetState(context, expression, display, shouldReplace)
            
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, CalculatorWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_calculator)
        val expression = CalculatorState.getExpression(context)
        val display = context.getSharedPreferences(CalculatorState.WIDGET_PREFS, Context.MODE_PRIVATE)
                .getString(CalculatorState.PREF_DISPLAY, "0") ?: "0"
        val shouldReplace = context.getSharedPreferences(CalculatorState.WIDGET_PREFS, Context.MODE_PRIVATE)
                .getBoolean(CalculatorState.PREF_SHOULD_REPLACE, false)

        views.setTextViewText(R.id.widget_expression, if (shouldReplace) expression else "")
        views.setTextViewText(R.id.widget_display, display)

        val keys = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "−", "×", "÷", "=", "C")
        val keyIds = mapOf(
            "0" to R.id.btn_0, "1" to R.id.btn_1, "2" to R.id.btn_2, "3" to R.id.btn_3,
            "4" to R.id.btn_4, "5" to R.id.btn_5, "6" to R.id.btn_6, "7" to R.id.btn_7,
            "8" to R.id.btn_8, "9" to R.id.btn_9, "+" to R.id.btn_add, "−" to R.id.btn_sub,
            "×" to R.id.btn_mul, "÷" to R.id.btn_div, "=" to R.id.btn_eq, "C" to R.id.btn_c
        )

        for (key in keys) {
            val id = keyIds[key] ?: continue
            val intent = Intent(context, CalculatorWidgetProvider::class.java).apply {
                action = ACTION_KEY_PRESS
                putExtra(EXTRA_KEY, key)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(id, pendingIntent)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        private const val ACTION_KEY_PRESS = "com.example.calculator.ACTION_KEY_PRESS"
        private const val EXTRA_KEY = "extra_key"
    }
}
