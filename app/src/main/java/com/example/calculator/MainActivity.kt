package com.example.calculator

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.toSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import android.graphics.Rect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.toRect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.ui.theme.AppBlack
import com.example.calculator.ui.theme.AppBlackSoft
import com.example.calculator.ui.theme.AppGray
import com.example.calculator.ui.theme.AppGrayBorder
import com.example.calculator.ui.theme.AppGrayMuted
import com.example.calculator.ui.theme.AppGreen
import com.example.calculator.ui.theme.AppGreenDeep
import com.example.calculator.ui.theme.AppGreenSoft
import com.example.calculator.ui.theme.AppWhite
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.R
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

// ---------------------------------------------------------------------------
// Puns
// ---------------------------------------------------------------------------

private const val PUN_PREFS = "pun_prefs"
private const val LAST_PUN_KEY = "last_pun"
private val PUNS = listOf(
    "Let's make this a sum-thing special.",
    "You can count on me.",
    "No need to divide and conquer.",
    "I'm positive this will add up.",
    "Stay sharp — don't lose your angle.",
    "I've got your number.",
    "Life is full of variables — I'm the constant.",
    "You're acute one.",
    "Don't be so negative — be rational.",
    "I'm rooting for you.",
    "We're a perfect pair — no remainder.",
    "Let's not make this a complex problem.",
    "I find you very a-mean-ingful.",
    "You've got infinite potential.",
    "Stop being so irrational.",
    "I'll always be in your corner — right angle and all.",
    "We're on the same plane.",
    "This relationship has a great slope.",
    "I'm over the moon — past the asymptote.",
    "You really know how to push my limits.",
    "Let's integrate our lives together.",
    "Our love is exponential.",
    "I'm totally in my element — periodic and all.",
    "You factor into everything I do.",
    "I never want to reach our limit.",
    "That solution was derived with care.",
    "No need to go off on a tangent.",
    "You've got a great range.",
    "I find you absolutely... absolute value.",
    "Don't be a zero — be the hero.",
    "Two wrongs don't make a right, but two negatives make a positive.",
    "I'm prime and ready.",
    "You complete my number line.",
    "Together we're greater than the sum of our parts.",
    "I'd never subtract you from my life.",
    "Let's multiply our happiness.",
    "You're my missing variable.",
    "I can't function without you.",
    "You're the square root of my happiness.",
    "Sine of the times — this is great.",
    "Cos I care about you.",
    "That really adds a new dimension.",
    "You make every problem seem solvable.",
    "Life without you would be undefined.",
    "I've calculated all outcomes — you're the best.",
    "Don't worry, I've got this figured out to the last decimal.",
    "You're worth more than pi.",
    "I love you to infinity and beyond the number line.",
    "We've got great chemistry — and the math checks out.",
    "Keep calm and carry the one.",
    "I'm not obtuse — I know a good thing when I see it.",
    "You really raised my average.",
    "That idea has a lot of volume.",
    "Let's not make this a recurring problem.",
    "I've got a good feeling about this — call it my gut instinct, or my gut-enberg theorem.",
    "You're never odd or even — you're just right.",
    "I could talk to you for an infinite series.",
    "Don't worry, this problem is well within your domain.",
    "You really know how to work a formula.",
    "I'm totally rational — most of the time.",
    "Every angle of this plan looks solid.",
    "You're the denominator that keeps me grounded.",
    "My love for you has no upper bound.",
    "That was a calculated risk — totally worth it.",
    "You've solved for the unknown in my heart.",
    "I'm wholly devoted — no fractions about it.",
    "You're proof that good things come in natural numbers.",
    "The math is simple: me plus you equals everything.",
    "I'd never want to find the limits of our friendship.",
    "You're an original — not a copy or a translation.",
    "We've got great symmetry.",
    "I'm not just winging it — I've done the derivation.",
    "You had me at 'equals.'",
    "Let's make today count — literally.",
    "This moment is statistically significant.",
    "You're the x to my y-axis.",
    "I'm positively charged — must be the positive integers.",
    "We make a great set — no elements missing.",
    "Every problem has a solution when you're around.",
    "You're a real number — not imaginary at all.",
    "I mean this in the most geometric sense: you're well-rounded.",
    "Our bond is stronger than any postulate.",
    "I've proven my hypothesis — you're amazing.",
    "You're complementary in every angle.",
    "Don't worry, I've got the whole equation balanced.",
    "Life's a graph — and things are trending up.",
    "You're the right answer I didn't know I was solving for.",
    "I'm fluent in the language of numbers — and you're my favorite expression.",
    "You've got a great arc to your story.",
    "I never lose my train of thought — just my place in the sequence.",
    "You're the constant that makes my equation work.",
    "I've run the numbers — this is a great idea.",
    "Nothing about you is average.",
    "You're divisible by awesome.",
    "I'd walk a million units just to see you.",
    "You've got the golden ratio of personality.",
    "This plan has a beautiful proof.",
    "I'm not just theorizing — the data supports it."
)

private fun nextPun(context: Context): String {
    val prefs = context.getSharedPreferences(PUN_PREFS, Context.MODE_PRIVATE)
    val lastPun = prefs.getString(LAST_PUN_KEY, null)
    val nextPun = PUNS.filterNot { it == lastPun }.random()
    prefs.edit().putString(LAST_PUN_KEY, nextPun).apply()
    return nextPun
}

// ---------------------------------------------------------------------------
// Settings — sqrt mode
// ---------------------------------------------------------------------------

private const val SETTINGS_PREFS = "user_settings_prefs"
private const val SQRT_MODE_KEY = "sqrt_mode"
private const val GESTURE_TYPING_KEY = "gesture_typing_enabled"
private const val SHORTCUT_THRESHOLD_KEY = "shortcut_threshold"

private enum class SqrtMode {
    WrapCurrentInput,
    AppendToEnd,
    AskEveryTime
}

private fun loadGestureTypingEnabled(context: Context): Boolean {
    val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
    return prefs.getBoolean(GESTURE_TYPING_KEY, true)
}

private fun saveGestureTypingEnabled(context: Context, enabled: Boolean) {
    context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(GESTURE_TYPING_KEY, enabled)
        .apply()
}

private fun loadShortcutThreshold(context: Context): Int {
    val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
    return prefs.getInt(SHORTCUT_THRESHOLD_KEY, 5)
}

private fun saveShortcutThreshold(context: Context, threshold: Int) {
    context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        .edit()
        .putInt(SHORTCUT_THRESHOLD_KEY, threshold)
        .apply()
}

private fun loadSqrtMode(context: Context): SqrtMode {
    val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
    return when (prefs.getString(SQRT_MODE_KEY, SqrtMode.AskEveryTime.name)) {
        SqrtMode.WrapCurrentInput.name -> SqrtMode.WrapCurrentInput
        SqrtMode.AppendToEnd.name -> SqrtMode.AppendToEnd
        else -> SqrtMode.AskEveryTime
    }
}

private fun saveSqrtMode(context: Context, mode: SqrtMode) {
    context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        .edit()
        .putString(SQRT_MODE_KEY, mode.name)
        .apply()
}

// ---------------------------------------------------------------------------
// Settings — variables
// ---------------------------------------------------------------------------

private const val VARS_PREFS = "user_vars_prefs"
internal val VAR_NAMES = (1..10).map { "var$it" }           // "var1".."var10"
internal const val ANS_VAR = "ans"

// Tokens used in expressions, e.g. "$var1", "$ans"
internal fun varToken(name: String) = "\$$name"

private fun loadAllVariables(context: Context): MutableMap<String, Double?> {
    val prefs = context.getSharedPreferences(VARS_PREFS, Context.MODE_PRIVATE)
    val map = mutableMapOf<String, Double?>()
    (VAR_NAMES + ANS_VAR).forEach { name ->
        map[name] = prefs.getString(name, null)?.toDoubleOrNull()
    }
    return map
}

private fun persistVariable(context: Context, name: String, value: Double?) {
    val editor = context.getSharedPreferences(VARS_PREFS, Context.MODE_PRIVATE).edit()
    if (value == null) editor.remove(name) else editor.putString(name, value.toBigDecimal().toPlainString())
    editor.apply()
}

// ---------------------------------------------------------------------------
// Settings — shortcuts
// ---------------------------------------------------------------------------

private const val SHORTCUTS_PREFS = "user_shortcuts_prefs"

private fun loadShortcuts(context: Context): MutableMap<String, String> {
    val prefs = context.getSharedPreferences(SHORTCUTS_PREFS, Context.MODE_PRIVATE)
    val map = mutableMapOf<String, String>()
    prefs.all.forEach { (key, value) ->
        if (value is String) map[key] = value
    }
    return map
}

private fun persistShortcut(context: Context, name: String, value: String?) {
    val editor = context.getSharedPreferences(SHORTCUTS_PREFS, Context.MODE_PRIVATE).edit()
    if (value == null) editor.remove(name) else editor.putString(name, value)
    editor.apply()
}

// ---------------------------------------------------------------------------
// Number formatting — shared top-level helper
// ---------------------------------------------------------------------------

internal fun formatNumber(v: Double): String =
    if (v == floor(v) && !v.isInfinite()) v.toLong().toString()
    else "%.10g".format(v).trimEnd('0').trimEnd('.')

// ---------------------------------------------------------------------------
// Tabs
// ---------------------------------------------------------------------------

private enum class MainTab { Calculator, UserSettings }

// ---------------------------------------------------------------------------
// Activity
// ---------------------------------------------------------------------------

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.white)
        // enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                CalculatorScreen()
            }
        }
    }
}

// ---------------------------------------------------------------------------
// ViewModel
// ---------------------------------------------------------------------------

class CalculatorViewModel(app: Application) : AndroidViewModel(app) {

    private val ctx get() = getApplication<Application>()
    private val historyManager = HistoryManager(ctx)

    // ── state ──────────────────────────────────────────────────────────────
    var expression by mutableStateOf("")
        private set

    var display by mutableStateOf("0")
        private set

    val variables = mutableStateMapOf<String, Double?>()
    val shortcuts = mutableStateMapOf<String, String>()
    
    var suggestedShortcut by mutableStateOf<String?>(null)
        private set

    var shouldReplaceOnNextInput by mutableStateOf(false)
        private set
        
    // For gesture typing
    val buttonBounds = mutableStateMapOf<String, androidx.compose.ui.geometry.Rect>()

    /**
     * Character offset in [expression] where the next insertion/deletion will happen.
     * null  →  append-mode (default): new input goes to the end.
     * non-null  →  cursor-mode: all input is inserted at this exact position.
     *
     * Set by [setCursor] when the user taps a display token.
     */
    var cursorPosition by mutableStateOf<Int?>(null)
        private set

    /**
     * The index of the display token whose END sits at [cursorPosition].
     * Drives the green highlight in the UI.  Read-only computed value (not stored as state –
     * Compose automatically tracks the mutableStateOf reads inside this getter).
     */
    var highlightedTokenIndex: Int? = null
        get() {
            val pos = cursorPosition ?: return null
            val tokens = tokenizeForDisplay(expression)
            if (tokens.isEmpty()) return null
            var offset = 0
            for ((idx, tok) in tokens.withIndex()) {
                offset += tok.length
                if (pos <= offset) return idx
            }
            return tokens.lastIndex
        }

    var previousExpression by mutableStateOf<String?>(null)
        private set
        
    var isGestureTypingEnabled by mutableStateOf(true)
    var shortcutThreshold by mutableStateOf(5)

    init {
        variables.putAll(loadAllVariables(ctx))
        shortcuts.putAll(loadShortcuts(ctx))
        isGestureTypingEnabled = loadGestureTypingEnabled(ctx)
        shortcutThreshold = loadShortcutThreshold(ctx)
    }

    // ── public helpers ─────────────────────────────────────────────────────

    fun setVariable(name: String, value: Double?) {
        variables[name] = value
        persistVariable(ctx, name, value)
    }

    fun setShortcut(name: String, value: String?) {
        if (value == null) {
            shortcuts.remove(name)
        } else {
            shortcuts[name] = value
        }
        persistShortcut(ctx, name, value)
        if (value != null && suggestedShortcut == value) {
            suggestedShortcut = null
            historyManager.clearFrequency(value)
        }
    }
    
    fun dismissSuggestion() {
        val suggested = suggestedShortcut ?: return
        historyManager.clearFrequency(suggested)
        suggestedShortcut = null
    }

    /** Insert a variable token (e.g. "$var1") into the expression. */
    fun insertVariable(varName: String) {
        val token = varToken(varName)
        if (shouldReplaceOnNextInput) {
            expression = token
            shouldReplaceOnNextInput = false
        } else {
            expression += token
        }
        display = expression
    }
    
    fun insertShortcut(name: String) {
        val token = varToken(name)
        if (shouldReplaceOnNextInput) {
            expression = token
            shouldReplaceOnNextInput = false
        } else {
            expression += token
        }
        display = expression
    }

    /** Insert the assignment operator " = " into the expression. */
    fun insertAssignEquals() {
        expression += " = "
        display = expression
        shouldReplaceOnNextInput = false
    }

    // ── key handler ────────────────────────────────────────────────────────

    fun onKey(key: String) {
        // ── Cursor mode: insert / delete at the cursor offset ────────────────
        val cur = cursorPosition
        if (cur != null) {
            handleKeyAtCursor(key, cur)
            return
        }

        when (key) {
            "C" -> {
                expression = ""
                display = "0"
                shouldReplaceOnNextInput = false
                previousExpression = null
                cursorPosition = null
            }

            "⌫" -> {
                if (expression.isNotEmpty()) {
                    // Delete whole variable token if the expression ends with one
                    val endingToken = knownVarTokens().firstOrNull { expression.endsWith(it) }
                    expression = when {
                        endingToken != null -> expression.dropLast(endingToken.length)
                        // Also delete " = " assignment operator as a unit
                        expression.endsWith(" = ") -> expression.dropLast(3)
                        else -> expression.dropLast(1)
                    }
                    display = if (expression.isEmpty()) "0" else expression
                }
                shouldReplaceOnNextInput = false
            }

            "=" -> {
                // ── Repeat: = pressed right after a previous result ───────
                if (shouldReplaceOnNextInput) {
                    performRepeat()
                    return
                }

                val trimmed = expression.trim()

                // ── Assignment: $varN = <rhs> ──────────────────────────────
                val assignMatch = Regex("""^\$(\w+)\s*=\s*(.+)$""").matchEntire(trimmed)
                if (assignMatch != null) {
                    val varName = assignMatch.groupValues[1]
                    val rhs = assignMatch.groupValues[2].trim()
                    if (isValidVarName(varName)) {
                        val result = safeEvaluate(trimTrailingOperator(rhs))
                        if (result != "Error") {
                            setVariable(varName, result.toDoubleOrNull())
                            display = "${varToken(varName)} = $result"
                        } else {
                            display = "Error"
                        }
                        expression = ""
                        shouldReplaceOnNextInput = false
                        return
                    }
                }

                // ── Normal evaluation ──────────────────────────────────────
                val exprToCapture = expression
                val result = safeEvaluate(trimTrailingOperator(expression))
                if (result != "Error") {
                    previousExpression = exprToCapture
                    setVariable(ANS_VAR, result.toDoubleOrNull())
                    
                    // Frequency tracking for shortcuts
                    val op = trimTrailingOperator(exprToCapture)
                    if (op.length > 2) { // Only track reasonably complex operations
                        historyManager.incrementFrequency(op)
                        val freq = historyManager.getFrequency(op)
                        if (freq >= shortcutThreshold && !shortcuts.values.contains(op)) {
                            suggestedShortcut = op
                        }
                    }
                }
                display = result
                expression = if (result == "Error") "" else result
                shouldReplaceOnNextInput = result != "Error"
                cursorPosition = null
            }

            "+/-" -> {
                if (expression.isNotEmpty()) {
                    expression = if (expression.startsWith("-")) expression.drop(1) else "-$expression"
                    display = expression
                }
                shouldReplaceOnNextInput = false
            }

            "√" -> appendSqrtToEnd()

            "%" -> {
                expression += "%"
                display = expression
                shouldReplaceOnNextInput = false
            }

            "xʸ" -> {
                appendOperator("^")
                display = expression
                shouldReplaceOnNextInput = false
            }

            "÷" -> { appendOperator("÷"); display = expression; shouldReplaceOnNextInput = false }
            "×" -> { appendOperator("×"); display = expression; shouldReplaceOnNextInput = false }
            "−" -> { appendOperator("−"); display = expression; shouldReplaceOnNextInput = false }
            "+" -> { appendOperator("+"); display = expression; shouldReplaceOnNextInput = false }

            else -> {
                val isNumericStart = key.firstOrNull()?.isDigit() == true || key == "."
                if (shouldReplaceOnNextInput && isNumericStart) {
                    expression = if (key == ".") "0." else key
                    shouldReplaceOnNextInput = false
                } else {
                    if (key == "." && (expression.isEmpty() || expression.last() in setOf('+', '-', '×', '÷', '^'))) {
                        expression += "0"
                    }
                    expression += key
                }
                display = expression
            }
        }
    }

    fun applySqrtWrap() {
        val base = trimTrailingOperator(expression)
        expression = if (base.isEmpty()) "√" else "√($base)"
        display = expression
        shouldReplaceOnNextInput = false
    }

    fun appendSqrtToEnd() {
        expression += "√"
        display = expression
        shouldReplaceOnNextInput = false
    }

    /** Load the previous expression back into the input without evaluating it. */
    fun loadPreviousExpression() {
        val prev = previousExpression ?: return
        expression = prev
        display = prev
        shouldReplaceOnNextInput = false
        cursorPosition = null
    }

    // ── token display & cursor helpers ─────────────────────────────────────

    /**
     * Splits [expr] into display tokens, preserving original Unicode operators.
     * Numbers (consecutive digit/dot runs) are grouped into a single token.
     * Variable tokens ($ans, $var1 …) are kept whole.
     * Shortcut tokens are also kept whole.
     * Every other character becomes its own single-char token.
     */
    fun tokenizeForDisplay(expr: String = expression): List<String> {
        if (expr.isEmpty()) return emptyList()
        val result = mutableListOf<String>()
        var i = 0
        while (i < expr.length) {
            when {
                expr[i] == '$' -> {
                    val sb = StringBuilder("\$")
                    i++
                    while (i < expr.length && (expr[i].isLetter() || expr[i].isDigit())) sb.append(expr[i++])
                    result += sb.toString()
                }
                expr[i].isDigit() || expr[i] == '.' -> {
                    val sb = StringBuilder()
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) sb.append(expr[i++])
                    result += sb.toString()
                }
                else -> { result += expr[i].toString(); i++ }
            }
        }
        return result
    }

    /**
     * Places the cursor at the END of the token at [tokenIndex] in the expression string,
     * enabling cursor-mode.  Tapping the already-highlighted token toggles back to append mode.
     * Pass null to force append mode.
     */
    fun setCursor(tokenIndex: Int?) {
        if (tokenIndex == null) { cursorPosition = null; return }
        val tokens = tokenizeForDisplay(expression)
        if (tokenIndex >= tokens.size) { cursorPosition = null; return }
        val newPos = tokens.take(tokenIndex + 1).sumOf { it.length }
        cursorPosition = if (cursorPosition == newPos) null else newPos   // toggle
    }

    /**
     * Handles any button press while [cursorPosition] is non-null.
     *
     * Model: pure string insertion / deletion at the cursor character offset — no smart
     * operator-replacement or token-awareness.  Every key inserts its characters literally
     * at the current position and advances the cursor by that many characters.
     *
     * The only exceptions are lifecycle keys (C, =, +/-) and ⌫.
     */
    private fun handleKeyAtCursor(key: String, cur: Int) {
        when (key) {
            "C" -> {
                expression = ""; display = "0"
                shouldReplaceOnNextInput = false; previousExpression = null
                cursorPosition = null; return
            }
            "=" -> { cursorPosition = null; onKey("="); return }
            "+/-" -> {
                // No clean cursor-aware sign toggle — fall back to global behaviour.
                cursorPosition = null; onKey("+/-"); return
            }
            "⌫" -> {
                if (cur == 0) { cursorPosition = null; return }
                val before = expression.substring(0, cur)
                val varTok = knownVarTokens().firstOrNull { before.endsWith(it) }
                val deleteLen = when {
                    varTok != null          -> varTok.length
                    before.endsWith(" = ") -> 3
                    else                   -> 1
                }
                expression = expression.substring(0, cur - deleteLen) + expression.substring(cur)
                cursorPosition = (cur - deleteLen).coerceAtLeast(0)
                display = if (expression.isEmpty()) "0" else expression
                return
            }
            else -> {
                val insert = when (key) {
                    "÷" -> "÷"; "×" -> "×"; "−" -> "−"; "+" -> "+"
                    "xʸ" -> "^"; "√" -> "√"; "%" -> "%"
                    "(" -> "("; ")" -> ")"
                    else -> key
                }
                expression = expression.substring(0, cur) + insert + expression.substring(cur)
                cursorPosition = cur + insert.length
                display = expression
            }
        }
    }

    // ── private helpers ────────────────────────────────────────────────────

    private fun isValidVarName(name: String): Boolean {
        if (name == ANS_VAR) return true
        val n = name.removePrefix("var").toIntOrNull() ?: return false
        return n in 1..10
    }

    /** All known variable tokens sorted longest-first for correct endsWith checks. */
    private fun knownVarTokens(): List<String> =
        (VAR_NAMES + ANS_VAR).map { varToken(it) }.sortedByDescending { it.length }

    private fun appendOperator(op: String) {
        if (expression.isEmpty() && op != "-") return
        if (expression.isNotEmpty() && expression.last() in setOf('+', '-', '×', '÷', '^')) {
            expression = expression.dropLast(1)
        }
        expression += op
    }

    private fun trimTrailingOperator(value: String): String =
        value.trimEnd { it in setOf('+', '-', '×', '÷', '^') }

    // ── repeat helpers ─────────────────────────────────────────────────────

    private fun performRepeat() {
        val ansValue = variables[ANS_VAR] ?: return
        val ansStr = CalculatorEngine.formatNumber(ansValue)

        // The previous expression that led to the current 'ans'
        val prevExpr = previousExpression ?: return
        
        // Find the operator and the operand from the previous expression
        // e.g., if prevExpr was "10 + 5", and ans is 15, we want to do "15 + 5"
        val match = Regex("""([+\-×÷^])\s*(\d*\.?\d+)$""").find(prevExpr.trim())
        val newExpr = if (match != null) {
            val op = match.groupValues[1]
            val operand = match.groupValues[2]
            ansStr + op + operand
        } else {
            // Fallback: try to substitute first number if it was a simple repeat before
            trySubstituteFirstNumber(expression, ansStr) ?: expression
        }

        val result = safeEvaluate(trimTrailingOperator(newExpr))
        if (result != "Error") {
            setVariable(ANS_VAR, result.toDoubleOrNull())
        }
        display = result
        expression = if (result == "Error") newExpr else result
        shouldReplaceOnNextInput = result != "Error"
    }

    private fun trySubstituteFirstNumber(expr: String, replacement: String): String? {
        val match = Regex("""^\d*\.?\d+""").find(expr) ?: return null
        val after = expr.substring(match.range.last + 1).trim()
        if (after.isEmpty() || !after.first().let { it in "+-×÷^" }) return null
        
        return replacement + expr.substring(match.range.last + 1)
    }

    private fun safeEvaluate(expr: String): String {
        return CalculatorEngine.safeEvaluate(expr, variables, shortcuts)
    }
}

// ---------------------------------------------------------------------------
// Root screen
// ---------------------------------------------------------------------------

@Composable
fun CalculatorScreen(vm: CalculatorViewModel = viewModel()) {
    val context = LocalContext.current
    val launcherPun = remember { nextPun(context) }
    var selectedTab by remember { mutableStateOf(MainTab.Calculator) }
    var sqrtMode by remember { mutableStateOf(loadSqrtMode(context)) }
    var pendingSqrtDialog by remember { mutableStateOf(false) }

    if (pendingSqrtDialog) {
        AlertDialog(
            onDismissRequest = { pendingSqrtDialog = false },
            title = { Text("Square root") },
            text = { Text("How should √ behave right now?") },
            confirmButton = {
                TextButton(onClick = { vm.applySqrtWrap(); pendingSqrtDialog = false }) {
                    Text("Wrap current input")
                }
            },
            dismissButton = {
                TextButton(onClick = { vm.appendSqrtToEnd(); pendingSqrtDialog = false }) {
                    Text("Add to end")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppWhite)
            .systemBarsPadding()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Header ─────────────────────────────────────────────────────────
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = launcherPun,
                    color = AppGreenDeep,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (selectedTab == MainTab.Calculator) "Calculator" else "User settings",
                    color = AppBlack,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = {
                selectedTab = if (selectedTab == MainTab.Calculator) MainTab.UserSettings else MainTab.Calculator
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Open settings",
                    tint = AppBlack
                )
            }
        }

        // ── Previous expression ── Outside scrollable elements
        val prevExpr = vm.previousExpression
        if (prevExpr != null && selectedTab == MainTab.Calculator) {
            Text(
                text = prevExpr,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { vm.loadPreviousExpression() }
                    .padding(horizontal = 4.dp),
                color = AppGrayMuted,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // ── Shortcut Suggestion ────────────────────────────────────────────
        val suggestion = vm.suggestedShortcut
        if (suggestion != null) {
            ShortcutSuggestionBanner(
                suggestion = suggestion,
                onAccept = { name -> vm.setShortcut(name, suggestion) },
                onDismiss = { vm.dismissSuggestion() }
            )
        }

        // ── Tab content ────────────────────────────────────────────────────
        when (selectedTab) {
            MainTab.Calculator -> CalculatorTabContent(
                vm = vm,
                onSqrtClick = {
                    when (sqrtMode) {
                        SqrtMode.WrapCurrentInput -> vm.applySqrtWrap()
                        SqrtMode.AppendToEnd -> vm.appendSqrtToEnd()
                        SqrtMode.AskEveryTime -> pendingSqrtDialog = true
                    }
                }
            )
            MainTab.UserSettings -> UserSettingsTabContent(
                currentSqrtMode = sqrtMode,
                onModeSelected = { sqrtMode = it; saveSqrtMode(context, it) },
                variables = vm.variables,
                onVariableSet = { name, value -> vm.setVariable(name, value) },
                vm = vm
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Calculator tab
// ---------------------------------------------------------------------------

private data class CalcKey(
    val label: String,
    val style: KeyStyle = KeyStyle.Default,
    val widthWeight: Float = 1f
)

private enum class KeyStyle { Default, Digit, Utility, Operation, Equal }

@Composable
private fun ColumnScope.CalculatorTabContent(
    vm: CalculatorViewModel,
    onSqrtClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clip(RoundedCornerShape(28.dp))
            .background(AppGray)
            .border(1.dp, AppGrayBorder, RoundedCornerShape(28.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ── Display ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(AppWhite)
                .border(1.dp, AppGrayBorder, RoundedCornerShape(22.dp))
                // Tapping empty space in the display → deselect (back to append mode)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { vm.setCursor(null) }
                .padding(18.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(16.dp))

                // ── Bottom area: interactive token row  OR  plain result ───
                val showTokens = vm.expression.isNotEmpty() && !vm.shouldReplaceOnNextInput
                if (showTokens) {
                    ExpressionTokenRow(
                        tokens = vm.tokenizeForDisplay(),
                        selectedIndex = vm.highlightedTokenIndex,
                        onTokenTap = { vm.setCursor(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (vm.cursorPosition != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "tap display to exit token edit",
                            color = AppGreen.copy(alpha = 0.55f),
                            fontSize = 11.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    AnimatedContent(
                        targetState = vm.display,
                        label = "displayTransition",
                        transitionSpec = {
                            fadeIn(animationSpec = tween(120)) togetherWith fadeOut(animationSpec = tween(90))
                        }
                    ) { displayValue ->
                        Text(
                            text = displayValue,
                            color = AppBlack,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            softWrap = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // ── Variable chip row ──────────────────────────────────────────────
        VariableChipRow(vm = vm)

        // ── Buttons ────────────────────────────────────────────────────────
        var containerPosition by remember { mutableStateOf(Offset.Zero) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned { containerPosition = it.positionInWindow() }
                .pointerInput(vm.isGestureTypingEnabled) {
                    if (!vm.isGestureTypingEnabled) return@pointerInput
                    var lastButton: String? = null
                    detectDragGestures(
                        onDragStart = { lastButton = null },
                        onDragEnd = { lastButton = null },
                        onDragCancel = { lastButton = null },
                        onDrag = { change, _ ->
                            val windowPos = containerPosition + change.position
                            val hit = vm.buttonBounds.entries.find { it.value.contains(windowPos) }
                            if (hit != null && hit.key != lastButton) {
                                vm.onKey(hit.key)
                                lastButton = hit.key
                            }
                        }
                    )
                }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                ButtonRow(
                    listOf(
                        CalcKey("C", KeyStyle.Utility), CalcKey("⌫", KeyStyle.Utility),
                        CalcKey("%", KeyStyle.Utility), CalcKey("√", KeyStyle.Utility)
                    ),
                    vm, onSqrtClick = onSqrtClick
                )
                ButtonRow(listOf(CalcKey("7"), CalcKey("8"), CalcKey("9"), CalcKey("÷", KeyStyle.Operation)), vm)
                ButtonRow(listOf(CalcKey("4"), CalcKey("5"), CalcKey("6"), CalcKey("×", KeyStyle.Operation)), vm)
                ButtonRow(
                    listOf(CalcKey("1"), CalcKey("2"), CalcKey("3")), vm,
                    operatorKey = CalcKey("(", KeyStyle.Operation),
                    extraKey = CalcKey("−", KeyStyle.Operation)
                )
                ButtonRow(
                    listOf(CalcKey("0", widthWeight = 2f), CalcKey(".")), vm,
                    operatorKey = CalcKey(")", KeyStyle.Operation),
                    extraKey = CalcKey("+", KeyStyle.Operation)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CalculatorButton(
                        text = "xʸ", style = KeyStyle.Operation,
                        modifier = Modifier.weight(1f).height(60.dp),
                        onClick = { vm.onKey("xʸ") }
                    )
                    CalculatorButton(
                        text = "=", style = KeyStyle.Equal,
                        modifier = Modifier.weight(2f).height(60.dp),
                        onClick = { vm.onKey("=") }
                    )
                }
            }
        }
    }
}

@Composable
private fun ShortcutSuggestionBanner(
    suggestion: String,
    onAccept: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var shortcutName by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppGreenSoft)
            .border(1.dp, AppGreen, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Hey, it seems that you used this equation 20 times, do you want to create a shortcut for that?",
            color = AppGreenDeep,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = suggestion,
            color = AppBlack,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = shortcutName,
                onValueChange = { shortcutName = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Shortcut name (e.g. VAT)") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppGreen,
                    unfocusedBorderColor = AppGreen.copy(alpha = 0.5f)
                )
            )
            TextButton(onClick = { if (shortcutName.isNotBlank()) onAccept(shortcutName) }) {
                Text("Create", color = AppGreenDeep, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = AppGrayMuted)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Variable chip row
// ---------------------------------------------------------------------------

@Composable
private fun VariableChipRow(vm: CalculatorViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // " = " assignment operator chip
        AssignEqualsChip(onClick = { vm.insertAssignEquals() })

        // $ans
        val ansValue = vm.variables[ANS_VAR]
        VariableChip(
            chipLabel = "\$ans",
            valueLabel = if (ansValue != null) formatNumber(ansValue) else "—",
            isAns = true,
            onClick = { vm.insertVariable(ANS_VAR) }
        )

        // $var1..$var10
        VAR_NAMES.forEachIndexed { index, name ->
            val value = vm.variables[name]
            VariableChip(
                chipLabel = "\$${index + 1}",
                valueLabel = if (value != null) formatNumber(value) else "—",
                isAns = false,
                onClick = { vm.insertVariable(name) }
            )
        }
        
        // Shortcuts
        vm.shortcuts.forEach { (name, value) ->
            VariableChip(
                chipLabel = "\$$name",
                valueLabel = value,
                isAns = false,
                onClick = { vm.insertShortcut(name) }
            )
        }
    }
}

@Composable
private fun VariableChip(
    chipLabel: String,
    valueLabel: String,
    isAns: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isAns) AppGreenSoft else AppWhite
    val border = if (isAns) AppGreen else AppGrayBorder

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Text(
            text = chipLabel,
            color = if (isAns) AppGreenDeep else AppBlack,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = valueLabel,
            color = if (isAns) AppGreenDeep else AppGrayMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AssignEqualsChip(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AppGreen)
            .border(1.dp, AppGreenDeep, RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "=",
            color = AppWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

// ---------------------------------------------------------------------------
// Expression token row (interactive expression display)
// ---------------------------------------------------------------------------

/**
 * Renders [tokens] as a horizontally-scrollable row of individually tappable chips.
 *
 * • The selected chip (matching [selectedIndex]) is highlighted in green.
 * • Tapping a chip fires [onTokenTap] with the chip's index.
 * • Tapping the already-selected chip calls [onTokenTap] with the same index,
 *   which the ViewModel interprets as a toggle → deselect.
 *
 * The row auto-scrolls to the right-most token whenever the expression grows
 * (append-mode), and to the selected token when one is active.
 */
@Composable
private fun ExpressionTokenRow(
    tokens: List<String>,
    selectedIndex: Int?,
    onTokenTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Auto-scroll: stay at the end in append mode; don't jump when cursor is active.
    LaunchedEffect(tokens.size, selectedIndex) {
        if (selectedIndex == null) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Row(
        modifier = modifier
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tokens.forEachIndexed { index, token ->
            val isSelected = index == selectedIndex
            // Skip plain space tokens visually (they keep spacing via the row arrangement)
            val displayText = if (token == " ") "\u2009" else token   // thin-space placeholder

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) AppGreenSoft else Color.Transparent)
                    .then(
                        if (isSelected)
                            Modifier.border(1.dp, AppGreen, RoundedCornerShape(8.dp))
                        else
                            Modifier
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember(index) { MutableInteractionSource() }
                    ) { onTokenTap(index) }
                    .padding(horizontal = 2.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    color = if (isSelected) AppGreenDeep else AppBlack,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Settings tab
// ---------------------------------------------------------------------------

@Composable
private fun UserSettingsTabContent(
    currentSqrtMode: SqrtMode,
    onModeSelected: (SqrtMode) -> Unit,
    variables: Map<String, Double?>,
    onVariableSet: (String, Double?) -> Unit,
    vm: CalculatorViewModel
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(AppGray)
            .border(1.dp, AppGrayBorder, RoundedCornerShape(24.dp))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Sqrt section ───────────────────────────────────────────────────
        Text("Square root behavior", color = AppBlackSoft, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

        SettingsChoiceRow(
            title = "Always add square root around item",
            subtitle = "Wrap the current expression with √(…)",
            selected = currentSqrtMode == SqrtMode.WrapCurrentInput,
            onClick = { onModeSelected(SqrtMode.WrapCurrentInput) }
        )
        SettingsChoiceRow(
            title = "Always add square root at the end",
            subtitle = "Keep the current append behavior",
            selected = currentSqrtMode == SqrtMode.AppendToEnd,
            onClick = { onModeSelected(SqrtMode.AppendToEnd) }
        )
        SettingsChoiceRow(
            title = "Ask every time",
            subtitle = "Show a quick choice when you tap √",
            selected = currentSqrtMode == SqrtMode.AskEveryTime,
            onClick = { onModeSelected(SqrtMode.AskEveryTime) }
        )

        Spacer(Modifier.height(4.dp))

        // ── Variables section ──────────────────────────────────────────────
        Text("Variables", color = AppBlackSoft, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

        Text(
            text = "Tap a variable chip on the calculator to insert it into an expression. " +
                    "Use the = chip to write an assignment like \$var1 = 10.",
            color = AppGrayMuted,
            fontSize = 13.sp
        )

        VAR_NAMES.forEachIndexed { index, name ->
            VariableEditorRow(
                label = "\$var${index + 1}",
                value = variables[name],
                onSave = { onVariableSet(name, it) }
            )
        }

        // $ans — read-only, auto-set by calculations
        val ansValue = variables[ANS_VAR]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(AppGreenSoft)
                .border(1.dp, AppGreen, RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("\$ans", color = AppGreenDeep, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Auto-set by every calculation",
                    color = AppGreenDeep,
                    fontSize = 12.sp
                )
            }
            Text(
                text = if (ansValue != null) formatNumber(ansValue) else "—",
                color = AppGreenDeep,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(4.dp))

        // ── Gesture Typing Section ─────────────────────────────────────────
        Text("Gestures", color = AppBlackSoft, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        SettingsChoiceRow(
            title = "Enable slide-to-type",
            subtitle = "Type equations by sliding through buttons",
            selected = vm.isGestureTypingEnabled,
            onClick = {
                vm.isGestureTypingEnabled = !vm.isGestureTypingEnabled
                saveGestureTypingEnabled(context, vm.isGestureTypingEnabled)
            }
        )

        Spacer(Modifier.height(4.dp))

        // ── Shortcuts section ──────────────────────────────────────────────
        Text("Shortcuts", color = AppBlackSoft, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Detection threshold", color = AppBlackSoft, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (vm.shortcutThreshold > 1) {
                        vm.shortcutThreshold--
                        saveShortcutThreshold(context, vm.shortcutThreshold)
                    }
                }) { Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                
                Text(vm.shortcutThreshold.toString(), fontWeight = FontWeight.Bold)
                
                IconButton(onClick = {
                    vm.shortcutThreshold++
                    saveShortcutThreshold(context, vm.shortcutThreshold)
                }) { Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            }
        }

        if (vm.shortcuts.isEmpty()) {
            Text("No shortcuts created yet.", color = AppGrayMuted, fontSize = 13.sp)
        }

        vm.shortcuts.forEach { (name, value) ->
            ShortcutEditorRow(
                name = name,
                value = value,
                onDelete = { vm.setShortcut(name, null) }
            )
        }
    }
}

@Composable
private fun ShortcutEditorRow(
    name: String,
    value: String,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppWhite)
            .border(1.dp, AppGrayBorder, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("\$$name", color = AppBlack, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(value, color = AppGrayMuted, fontSize = 13.sp)
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete shortcut",
                tint = Color.Red.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun VariableEditorRow(
    label: String,
    value: Double?,
    onSave: (Double?) -> Unit
) {
    var text by remember(value) { mutableStateOf(value?.let { formatNumber(it) } ?: "") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val commit = {
        val parsed = text.trim().toDoubleOrNull()
        if (text.trim().isEmpty()) onSave(null) else if (parsed != null) onSave(parsed)
        keyboardController?.hide()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppWhite)
            .border(1.dp, AppGrayBorder, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = AppBlack,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(52.dp)
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = { Text("unset", color = AppGrayMuted, fontSize = 14.sp) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { commit() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppGreen,
                unfocusedBorderColor = AppGrayBorder,
                focusedTextColor = AppBlack,
                unfocusedTextColor = AppBlack,
                cursorColor = AppGreen
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            shape = RoundedCornerShape(12.dp)
        )

        TextButton(
            onClick = { commit() },
            modifier = Modifier.wrapContentWidth()
        ) {
            Text("Set", color = AppGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

// ---------------------------------------------------------------------------
// Shared settings row
// ---------------------------------------------------------------------------

@Composable
private fun SettingsChoiceRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) AppGreenSoft else AppWhite)
            .border(1.dp, if (selected) AppGreen else AppGrayBorder, RoundedCornerShape(18.dp))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }, onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(18.dp)
                .width(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(if (selected) AppGreenDeep else AppGrayBorder)
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, color = AppBlack, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(text = subtitle, color = AppBlackSoft, fontSize = 13.sp)
        }
    }
}

// ---------------------------------------------------------------------------
// Calculator buttons
// ---------------------------------------------------------------------------

@Composable
private fun ButtonRow(
    keys: List<CalcKey>,
    vm: CalculatorViewModel,
    operatorKey: CalcKey? = null,
    extraKey: CalcKey? = null,
    onSqrtClick: (() -> Unit)? = null
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        keys.forEach { key ->
            CalculatorButton(
                text = key.label,
                style = key.style,
                vm = vm,
                modifier = Modifier.weight(key.widthWeight).height(60.dp),
                onClick = { if (key.label == "√") onSqrtClick?.invoke() else vm.onKey(key.label) }
            )
        }
        operatorKey?.let {
            CalculatorButton(
                text = it.label, style = it.style,
                vm = vm,
                modifier = Modifier.weight(it.widthWeight).height(60.dp),
                onClick = { vm.onKey(it.label) }
            )
        }
        extraKey?.let {
            CalculatorButton(
                text = it.label, style = it.style,
                vm = vm,
                modifier = Modifier.weight(it.widthWeight).height(60.dp),
                onClick = { vm.onKey(it.label) }
            )
        }
    }
}

@Composable
private fun CalculatorButton(
    text: String,
    style: KeyStyle,
    modifier: Modifier = Modifier,
    vm: CalculatorViewModel? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium),
        label = "btnScale"
    )
    val bg by animateColorAsState(
        targetValue = when (style) {
            KeyStyle.Equal -> if (pressed) AppGreenSoft else AppGreen
            KeyStyle.Operation, KeyStyle.Utility -> if (pressed) AppGray else AppGrayMuted
            KeyStyle.Digit, KeyStyle.Default -> if (pressed) AppGray else AppWhite
        },
        animationSpec = tween(140),
        label = "btnBg"
    )
    val textColor by animateColorAsState(
        targetValue = when (style) {
            KeyStyle.Equal -> AppWhite
            else -> AppBlack
        },
        animationSpec = tween(140),
        label = "btnText"
    )
    val borderColor = when (style) {
        KeyStyle.Equal -> AppGreenDeep
        KeyStyle.Operation -> AppGray
        KeyStyle.Utility, KeyStyle.Digit, KeyStyle.Default -> AppGrayBorder
    }

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .onGloballyPositioned { coords ->
                if (vm != null) {
                    vm.buttonBounds[text] = coords.positionInWindow()
                        .let { androidx.compose.ui.geometry.Rect(it, coords.size.toSize()) }
                }
            }
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = when (style) {
                KeyStyle.Utility -> 18.sp
                KeyStyle.Equal -> 24.sp
                else -> 22.sp
            },
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CalculatorPreview() {
    CalculatorTheme {
        CalculatorScreen()
    }
}
