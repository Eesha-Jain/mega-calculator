package com.example.calculator

import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

object CalculatorEngine {

    fun formatNumber(v: Double): String =
        if (v == floor(v) && !v.isInfinite()) v.toLong().toString()
        else "%.10g".format(v).trimEnd('0').trimEnd('.')

    fun safeEvaluate(expr: String, variables: Map<String, Double?>, shortcuts: Map<String, String> = emptyMap()): String {
        if (expr.isEmpty()) return "0"
        return try {
            val cleanedExpr = preProcessVoiceQuery(expr)
            val expandedExpr = expandShortcuts(cleanedExpr, shortcuts)
            val value = evaluate(expandedExpr, variables)
            if (value.isNaN() || value.isInfinite()) "Error" else formatNumber(value)
        } catch (_: Exception) {
            "Error"
        }
    }

    private fun preProcessVoiceQuery(query: String): String {
        return query.lowercase()
            .replace("square root", "√")
            .replace("root", "√")
            .replace("parentheses", "(")
            .replace("open bracket", "(")
            .replace("closed bracket", ")")
            .replace("close bracket", ")")
            .replace("to the power of", "^")
            .replace("times", "×")
            .replace("multiplied by", "×")
            .replace("divided by", "÷")
            .replace("plus", "+")
            .replace("minus", "−")
    }

    private fun expandShortcuts(expr: String, shortcuts: Map<String, String>): String {
        var currentExpr = expr
        val shortcutNames = shortcuts.keys.sortedByDescending { it.length }

        // Expand shortcuts iteratively in case of nested shortcuts (max 10 depth to prevent recursion).
        // Match whole shortcut tokens only so names like s1 do not accidentally replace parts of s10.
        for (i in 0 until 10) {
            var expanded = false
            for (name in shortcutNames) {
                val value = shortcuts[name] ?: continue
                val token = "\$$name"
                var searchStart = 0
                while (true) {
                    val index = currentExpr.indexOf(token, searchStart)
                    if (index < 0) break

                    val endIndex = index + token.length
                    val nextChar = currentExpr.getOrNull(endIndex)
                    if (nextChar == null || !nextChar.isLetterOrDigit()) {
                        currentExpr = currentExpr.substring(0, index) + value + currentExpr.substring(endIndex)
                        expanded = true
                        searchStart = index + value.length
                    } else {
                        searchStart = endIndex
                    }
                }
            }
            if (!expanded) break
        }
        return currentExpr
    }

    private fun evaluate(expr: String, variables: Map<String, Double?>): Double {
        val tokens = tokenize(expr)
        val pos = intArrayOf(0)
        val result = parseAddSub(tokens, pos, variables)
        if (pos[0] != tokens.size) throw IllegalArgumentException("Unexpected token")
        return result
    }

    private fun tokenize(expr: String): List<String> {
        val normalized = expr
            .replace('×', '*')
            .replace('÷', '/')
            .replace('−', '-')
        val list = mutableListOf<String>()
        var i = 0
        while (i < normalized.length) {
            val c = normalized[i]
            when {
                c == '$' -> {
                    val sb = StringBuilder()
                    sb.append('$')
                    i++
                    while (i < normalized.length && (normalized[i].isLetter() || normalized[i].isDigit())) {
                        sb.append(normalized[i++])
                    }
                    list += sb.toString()
                }
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < normalized.length && (normalized[i].isDigit() || normalized[i] == '.')) {
                        sb.append(normalized[i++])
                    }
                    list += sb.toString()
                }
                normalized.startsWith("sqrt", i) -> {
                    list += "√"
                    i += 4
                }
                c in listOf('+', '-', '*', '/', '^', '(', ')', '%', '√') -> {
                    list += c.toString()
                    i++
                }
                c.isWhitespace() || c == '=' -> i++
                else -> i++
            }
        }
        return list
    }

    private fun parseAddSub(tokens: List<String>, pos: IntArray, variables: Map<String, Double?>): Double {
        var value = parseMulDiv(tokens, pos, variables)
        while (pos[0] < tokens.size && tokens[pos[0]] in listOf("+", "-")) {
            val op = tokens[pos[0]++]
            val right = parseMulDiv(tokens, pos, variables)
            value = if (op == "+") value + right else value - right
        }
        return value
    }

    private fun parseMulDiv(tokens: List<String>, pos: IntArray, variables: Map<String, Double?>): Double {
        var value = parsePower(tokens, pos, variables)
        while (pos[0] < tokens.size) {
            when {
                tokens[pos[0]] in listOf("*", "/") -> {
                    val op = tokens[pos[0]++]
                    val right = parsePower(tokens, pos, variables)
                    value = if (op == "*") value * right else value / right
                }
                tokens[pos[0]].startsImplicitMultiplication() -> {
                    val right = parsePower(tokens, pos, variables)
                    value *= right
                }
                else -> return value
            }
        }
        return value
    }

    private fun parsePower(tokens: List<String>, pos: IntArray, variables: Map<String, Double?>): Double {
        var value = parseUnary(tokens, pos, variables)
        if (pos[0] < tokens.size && tokens[pos[0]] == "^") {
            pos[0]++
            val exponent = parsePower(tokens, pos, variables)
            value = value.pow(exponent)
        }
        return value
    }

    private fun parseUnary(tokens: List<String>, pos: IntArray, variables: Map<String, Double?>): Double {
        if (pos[0] >= tokens.size) throw IllegalArgumentException("Unexpected end")
        return when (tokens[pos[0]]) {
            "+" -> { pos[0]++; parseUnary(tokens, pos, variables) }
            "-" -> { pos[0]++; -parseUnary(tokens, pos, variables) }
            "√" -> { pos[0]++; sqrt(parseUnary(tokens, pos, variables)) }
            else -> parsePrimary(tokens, pos, variables)
        }
    }

    private fun parsePrimary(tokens: List<String>, pos: IntArray, variables: Map<String, Double?>): Double {
        if (pos[0] >= tokens.size) throw IllegalArgumentException("Unexpected end")
        val token = tokens[pos[0]++]
        return when {
            token == "(" -> {
                val value = parseAddSub(tokens, pos, variables)
                if (pos[0] >= tokens.size || tokens[pos[0]] != ")") {
                    throw IllegalArgumentException("Missing closing parenthesis")
                }
                pos[0]++
                applyPercentIfNeeded(tokens, pos, value)
            }
            token.startsWith("$") -> {
                val varName = token.drop(1)
                val value = variables[varName]
                    ?: throw IllegalArgumentException("Variable $token is not set")
                applyPercentIfNeeded(tokens, pos, value)
            }
            else -> {
                val value = token.toDouble()
                applyPercentIfNeeded(tokens, pos, value)
            }
        }
    }

    private fun applyPercentIfNeeded(tokens: List<String>, pos: IntArray, value: Double): Double {
        var result = value
        while (pos[0] < tokens.size && tokens[pos[0]] == "%") {
            pos[0]++
            result /= 100.0
        }
        return result
    }

    private fun String.startsImplicitMultiplication(): Boolean =
        this == "(" || this == "√" || firstOrNull()?.isDigit() == true || this == "." || startsWith("$")
}
