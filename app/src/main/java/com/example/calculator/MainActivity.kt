package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.ui.theme.CalculatorTheme
import kotlin.math.floor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                CalculatorScreen()
            }
        }
    }
}

// ── ViewModel ──────────────────────────────────────────────────────────────

class CalculatorViewModel : ViewModel() {

    var expression by mutableStateOf("")
        private set

    var display by mutableStateOf("0")
        private set

    fun onKey(key: String) {
        when (key) {
            "C" -> {
                expression = ""
                display = "0"
            }
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                    display = if (expression.isEmpty()) "0" else expression
                }
            }
            "=" -> {
                val result = safeEvaluate(expression)
                display = result
                expression = if (result == "Error") "" else result
            }
            "%" -> {
                val result = safeEvaluate(expression)
                if (result != "Error") {
                    val pct = safeEvaluate("$result/100")
                    display = pct
                    expression = if (pct == "Error") "" else pct
                }
            }
            "+/-" -> {
                if (expression.isNotEmpty()) {
                    expression = if (expression.startsWith("-")) {
                        expression.drop(1)
                    } else {
                        "-$expression"
                    }
                    display = expression
                }
            }
            else -> {
                // Don't allow starting with an operator (except -)
                if (expression.isEmpty() && key in listOf("+", "*", "/")) return
                // Replace a trailing operator with the new one
                if (expression.isNotEmpty() && key in listOf("+", "-", "*", "/")) {
                    if (expression.last() in listOf('+', '-', '*', '/')) {
                        expression = expression.dropLast(1)
                    }
                }
                expression += key
                display = expression
            }
        }
    }

    private fun safeEvaluate(expr: String): String {
        if (expr.isEmpty()) return "0"
        return try {
            val value = evaluate(expr)
            if (value.isInfinite() || value.isNaN()) "Error"
            else formatNumber(value)
        } catch (_: Exception) {
            "Error"
        }
    }

    private fun formatNumber(v: Double): String =
        if (v == floor(v) && !v.isInfinite()) v.toLong().toString()
        else "%.10g".format(v).trimEnd('0').trimEnd('.')

    // ── Recursive-descent arithmetic parser (+, -, *, /) ──────────────────

    private fun evaluate(expr: String): Double {
        val tokens = tokenize(expr)
        val pos = intArrayOf(0)
        val result = parseAddSub(tokens, pos)
        if (pos[0] != tokens.size) throw IllegalArgumentException("Unexpected token")
        return result
    }

    private fun tokenize(expr: String): List<String> {
        val list = mutableListOf<String>()
        var i = 0
        while (i < expr.length) {
            val c = expr[i]
            when {
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) {
                        sb.append(expr[i++])
                    }
                    list += sb.toString()
                }
                // Unary minus: appears at the start or right after another operator
                c == '-' && (list.isEmpty() || list.last() in listOf("+", "-", "*", "/")) -> {
                    val sb = StringBuilder("-")
                    i++
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) {
                        sb.append(expr[i++])
                    }
                    list += sb.toString()
                }
                c in listOf('+', '-', '*', '/') -> {
                    list += c.toString()
                    i++
                }
                else -> i++ // skip unrecognised characters
            }
        }
        return list
    }

    private fun parseAddSub(tokens: List<String>, pos: intArrayOf): Double {
        var left = parseMulDiv(tokens, pos)
        while (pos[0] < tokens.size && tokens[pos[0]] in listOf("+", "-")) {
            val op = tokens[pos[0]++]
            val right = parseMulDiv(tokens, pos)
            left = if (op == "+") left + right else left - right
        }
        return left
    }

    private fun parseMulDiv(tokens: List<String>, pos: intArrayOf): Double {
        var left = parseAtom(tokens, pos)
        while (pos[0] < tokens.size && tokens[pos[0]] in listOf("*", "/")) {
            val op = tokens[pos[0]++]
            val right = parseAtom(tokens, pos)
            left = if (op == "*") left * right else left / right
        }
        return left
    }

    private fun parseAtom(tokens: List<String>, pos: intArrayOf): Double {
        if (pos[0] >= tokens.size) throw IllegalArgumentException("Unexpected end of expression")
        return tokens[pos[0]++].toDouble()
    }
}

// ── Colours ────────────────────────────────────────────────────────────────

private val DarkBg      = Color(0xFF1C1C1E)
private val BtnNumber   = Color(0xFF333335)
private val BtnOperator = Color(0xFFFF9F0A)
private val BtnFunction = Color(0xFF3A3A3C)

// ── Screen ─────────────────────────────────────────────────────────────────

@Composable
fun CalculatorScreen(vm: CalculatorViewModel = viewModel()) {
    Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ── Display ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    if (vm.expression.isNotEmpty() && vm.display != vm.expression) {
                        Text(
                            text = vm.expression,
                            color = Color(0xFF8E8E93),
                            fontSize = 22.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    Text(
                        text = vm.display,
                        color = Color.White,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End
                    )
                }
            }

            // ── Button grid ──────────────────────────────────────────────
            val rows = listOf(
                listOf("C",   "⌫",   "%",  "/"),
                listOf("7",   "8",   "9",  "*"),
                listOf("4",   "5",   "6",  "-"),
                listOf("1",   "2",   "3",  "+"),
                listOf("+/-", "0",   ".",  "="),
            )
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { key ->
                        CalcKey(
                            label = key,
                            modifier = Modifier.weight(1f),
                            onClick = { vm.onKey(key) }
                        )
                    }
                }
            }
        }
    }
}

// ── Button ─────────────────────────────────────────────────────────────────

@Composable
private fun CalcKey(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val bg = when (label) {
        "=", "+", "-", "*", "/" -> BtnOperator
        "C", "⌫", "%", "+/-"  -> BtnFunction
        else                   -> BtnNumber
    }

    Button(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = bg),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = label,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

// ── Preview ────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun CalculatorPreview() {
    CalculatorTheme { CalculatorScreen() }
}
