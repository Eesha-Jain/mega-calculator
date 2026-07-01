
> Can you help me with 2 tasks?
> 2) The shortcut doesn't seem to work as intended. The shortcuts are supposed to be equation fragments like "+2". So if there is already 6 and then I add s1, so that shows 6 $s1, that should do 6 + 2 = 8.
> 3) I partially begun to fix it but it isn't complete: I noticed there is a bug. If I do 5(2+3), then it outputs 25 but if I do it when there is history, doing ( clears the expression. I'm not sure why it isn't recognizing that () and sqrt() can be added next to a number for multiplication when there is history and work when there isn't history
> 
> You should only need MainActivity.kt, CalculatorEngine.kt, and CalculatorState.kt. Let me know if you have any questions! Thank you so much :) Be curious and critical! Make sure to read through the files and develop a plan before going and editing both

Let me read through all three files first to understand the full picture before touching anything.

```xml
<function_calls>
<invoke name="ListFiles">
<parameter name="path">.</parameter>
</invoke>
</function_calls>
<function_results>
.git (directory)
.git\COMMIT_EDITMSG
.git\config
.git\description
.git\HEAD
.git\hooks (directory)
.git\index
.git\info (directory)
.git\logs (directory)
.git\objects (directory)
.git\refs (directory)
.gitignore
.gradle (directory)
.gradle\8.11.1 (directory)
.gradle\buildOutputCleanup (directory)
.gradle\file-system.probe
.gradle\vcs-1 (directory)
.idea (directory)
.idea\.gitignore
.idea\AndroidProjectSystem.xml
.idea\assetWizardSettings.xml
.idea\caches (directory)
.idea\compiler.xml
.idea\deploymentTargetSelector.xml
.idea\deviceManager.xml
.idea\gradle.xml
.idea\misc.xml
.idea\modules (directory)
.idea\runConfigurations.xml
.idea\studiobot.xml
.idea\vcs.xml
.idea\workspace.xml
.kotlin (directory)
.kotlin\sessions (directory)
app (directory)
app\.gitignore
app\build (directory)
app\build.gradle.kts
app\proguard-rules.pro
app\src (directory)
build (directory)
build\reports (directory)
build.gradle.kts
CALCULATOR_NOTES.md
docs (directory)
docs\LEARNING_GUIDE.md
exception-chat-history-1782864829802-sweet-puffin-26236.jsonl
exception-log-1782864830078-sweet-puffin.log
exception-log-1782864830078-sweet-puffin1.log
exception-trajectory-1782864829893-sweet-puffin-26236.md
gradle (directory)
gradle\gradle-daemon-jvm.properties
gradle\libs.versions.toml
gradle\wrapper (directory)
gradle.properties
gradlew.bat
local.properties
settings.gradle.kts
</function_results>
```
Calling function(s)...

```xml
<function_calls>
<invoke name="ListFiles">
<parameter name="path">app/src</parameter>
</invoke>
</function_calls>
<function_results>
main (directory)
main\AndroidManifest.xml
main\java (directory)
main\res (directory)
test (directory)
test\java (directory)
</function_results>
```
Calling function(s)...

```xml
<function_calls>
<invoke name="FindFiles">
<parameter name="filePatterns">[&quot;**/*.kt&quot;]</parameter>
</invoke>
</function_calls>
<function_results>
app\src\main\java\com\example\calculator\AppTileService.kt
app\src\main\java\com\example\calculator\CalculatorEngine.kt
app\src\main\java\com\example\calculator\CalculatorState.kt
app\src\main\java\com\example\calculator\CalculatorWidgetProvider.kt
app\src\main\java\com\example\calculator\HistoryManager.kt
app\src\main\java\com\example\calculator\MainActivity.kt
app\src\main\java\com\example\calculator\VoiceActivity.kt
app\src\main\java\com\example\calculator\ui\theme\Color.kt
app\src\main\java\com\example\calculator\ui\theme\Theme.kt
app\src\main\java\com\example\calculator\ui\theme\Type.kt
</function_results>
```
Calling function(s)...

```xml
<function_calls>
<invoke name="ViewFile">
<parameter name="path">app/src/main/java/com/example/calculator/MainActivity.kt</parameter>
<parameter name="lineNumbers">True</parameter>
</invoke>
</function_calls>
<function_results>
1: package com.example.calculator
  2: 
  3: import android.app.Application
  4: import android.content.Context
  5: import android.os.Bundle
  6: import androidx.activity.ComponentActivity
  7: import androidx.activity.compose.setContent
  8: import androidx.activity.enableEdgeToEdge
  9: import androidx.compose.animation.AnimatedContent
  10: import androidx.compose.animation.animateColorAsState
  11: import androidx.compose.animation.core.Spring
  12: import androidx.compose.animation.core.animateFloatAsState
  13: import androidx.compose.animation.core.spring
  14: import androidx.compose.animation.core.tween
  15: import androidx.compose.animation.fadeIn
  16: import androidx.compose.animation.fadeOut
  17: import androidx.compose.animation.togetherWith
  18: import androidx.compose.foundation.background
  19: import androidx.compose.foundation.border
  20: import androidx.compose.foundation.clickable
  21: import androidx.compose.foundation.horizontalScroll
  22: import androidx.compose.foundation.gestures.detectDragGestures
  23: import androidx.compose.ui.input.pointer.pointerInput
  24: import androidx.compose.ui.unit.toSize
  25: import androidx.compose.ui.input.pointer.util.VelocityTracker
  26: import androidx.compose.ui.input.pointer.PointerInputChange
  27: import androidx.compose.ui.input.pointer.PointerType
  28: import androidx.compose.ui.input.pointer.positionChange
  29: import kotlin.math.abs
  30: import androidx.compose.foundation.interaction.MutableInteractionSource
  31: import androidx.compose.foundation.interaction.collectIsPressedAsState
  32: import androidx.compose.foundation.layout.Arrangement
  33: import androidx.compose.foundation.layout.Box
  34: import androidx.compose.foundation.layout.Column
  35: import androidx.compose.foundation.layout.ColumnScope
  36: import androidx.compose.foundation.layout.Row
  37: import androidx.compose.foundation.layout.Spacer
  38: import androidx.compose.foundation.layout.fillMaxSize
  39: import androidx.compose.foundation.layout.fillMaxWidth
  40: import androidx.compose.foundation.layout.height
  41: import androidx.compose.foundation.layout.padding
  42: import androidx.compose.foundation.layout.systemBarsPadding
  43: import androidx.compose.foundation.layout.width
  44: import androidx.compose.foundation.layout.wrapContentWidth
  45: import androidx.compose.foundation.rememberScrollState
  46: import androidx.compose.foundation.shape.RoundedCornerShape
  47: import androidx.compose.foundation.text.KeyboardActions
  48: import androidx.compose.foundation.text.KeyboardOptions
  49: import androidx.compose.foundation.verticalScroll
  50: import androidx.compose.material.icons.Icons
  51: import androidx.compose.material.icons.filled.Delete
  52: import androidx.compose.material.icons.filled.Settings
  53: import androidx.compose.material3.AlertDialog
  54: import androidx.compose.material3.Icon
  55: import androidx.compose.material3.IconButton
  56: import androidx.compose.material3.MaterialTheme
  57: import androidx.compose.material3.OutlinedTextField
  58: import androidx.compose.material3.OutlinedTextFieldDefaults
  59: import androidx.compose.material3.Text
  60: import androidx.compose.material3.TextButton
  61: import androidx.compose.runtime.Composable
  62: import androidx.compose.runtime.LaunchedEffect
  63: import androidx.compose.runtime.getValue
  64: import android.graphics.Rect
  65: import androidx.compose.runtime.mutableStateMapOf
  66: import androidx.compose.runtime.mutableStateOf
  67: import androidx.compose.ui.geometry.Offset
  68: import androidx.compose.ui.layout.onGloballyPositioned
  69: import androidx.compose.ui.layout.positionInWindow
  70: import androidx.compose.ui.unit.IntRect
  71: import androidx.compose.ui.unit.toRect
  72: import androidx.compose.runtime.remember
  73: import androidx.compose.runtime.setValue
  74: import androidx.compose.ui.Alignment
  75: import androidx.compose.ui.Modifier
  76: import androidx.compose.ui.draw.alpha
  77: import androidx.compose.ui.draw.clip
  78: import androidx.compose.ui.draw.scale
  79: import androidx.compose.ui.graphics.Color
  80: import androidx.compose.ui.platform.LocalContext
  81: import androidx.compose.ui.platform.LocalSoftwareKeyboardController
  82: import androidx.compose.ui.text.font.FontWeight
  83: import androidx.compose.ui.text.input.ImeAction
  84: import androidx.compose.ui.text.input.KeyboardType
  85: import androidx.compose.ui.text.style.TextAlign
  86: import androidx.compose.ui.text.style.TextOverflow
  87: import androidx.compose.ui.tooling.preview.Preview
  88: import androidx.compose.ui.unit.dp
  89: import androidx.compose.ui.unit.sp
  90: import androidx.lifecycle.AndroidViewModel
  91: import androidx.lifecycle.viewmodel.compose.viewModel
  92: import com.example.calculator.ui.theme.AppBlack
  93: import com.example.calculator.ui.theme.AppBlackSoft
  94: import com.example.calculator.ui.theme.AppGray
  95: import com.example.calculator.ui.theme.AppGrayBorder
  96: import com.example.calculator.ui.theme.AppGrayMuted
  97: import com.example.calculator.ui.theme.AppGreen
  98: import com.example.calculator.ui.theme.AppGreenDeep
  99: import com.example.calculator.ui.theme.AppGreenSoft
  100: import com.example.calculator.ui.theme.AppWhite
  101: import com.example.calculator.ui.theme.CalculatorTheme
  102: import com.example.calculator.R
  103: import kotlin.math.floor
  104: import kotlin.math.pow
  105: import kotlin.math.sqrt
  106: 
  107: // ---------------------------------------------------------------------------
  108: // Puns
  109: // ---------------------------------------------------------------------------
  110: 
  111: private const val PUN_PREFS = &quot;pun_prefs&quot;
  112: private const val LAST_PUN_KEY = &quot;last_pun&quot;
  113: private val PUNS = listOf(
  114:     &quot;Let&apos;s make this a sum-thing special.&quot;,
  115:     &quot;You can count on me.&quot;,
  116:     &quot;No need to divide and conquer.&quot;,
  117:     &quot;I&apos;m positive this will add up.&quot;,
  118:     &quot;Stay sharp — don&apos;t lose your angle.&quot;,
  119:     &quot;I&apos;ve got your number.&quot;,
  120:     &quot;Life is full of variables — I&apos;m the constant.&quot;,
  121:     &quot;You&apos;re acute one.&quot;,
  122:     &quot;Don&apos;t be so negative — be rational.&quot;,
  123:     &quot;I&apos;m rooting for you.&quot;,
  124:     &quot;We&apos;re a perfect pair — no remainder.&quot;,
  125:     &quot;Let&apos;s not make this a complex problem.&quot;,
  126:     &quot;I find you very a-mean-ingful.&quot;,
  127:     &quot;You&apos;ve got infinite potential.&quot;,
  128:     &quot;Stop being so irrational.&quot;,
  129:     &quot;I&apos;ll always be in your corner — right angle and all.&quot;,
  130:     &quot;We&apos;re on the same plane.&quot;,
  131:     &quot;This relationship has a great slope.&quot;,
  132:     &quot;I&apos;m over the moon — past the asymptote.&quot;,
  133:     &quot;You really know how to push my limits.&quot;,
  134:     &quot;Let&apos;s integrate our lives together.&quot;,
  135:     &quot;Our love is exponential.&quot;,
  136:     &quot;I&apos;m totally in my element — periodic and all.&quot;,
  137:     &quot;You factor into everything I do.&quot;,
  138:     &quot;I never want to reach our limit.&quot;,
  139:     &quot;That solution was derived with care.&quot;,
  140:     &quot;No need to go off on a tangent.&quot;,
  141:     &quot;You&apos;ve got a great range.&quot;,
  142:     &quot;I find you absolutely... absolute value.&quot;,
  143:     &quot;Don&apos;t be a zero — be the hero.&quot;,
  144:     &quot;Two wrongs don&apos;t make a right, but two negatives make a positive.&quot;,
  145:     &quot;I&apos;m prime and ready.&quot;,
  146:     &quot;You complete my number line.&quot;,
  147:     &quot;Together we&apos;re greater than the sum of our parts.&quot;,
  148:     &quot;I&apos;d never subtract you from my life.&quot;,
  149:     &quot;Let&apos;s multiply our happiness.&quot;,
  150:     &quot;You&apos;re my missing variable.&quot;,
  151:     &quot;I can&apos;t function without you.&quot;,
  152:     &quot;You&apos;re the square root of my happiness.&quot;,
  153:     &quot;Sine of the times — this is great.&quot;,
  154:     &quot;Cos I care about you.&quot;,
  155:     &quot;That really adds a new dimension.&quot;,
  156:     &quot;You make every problem seem solvable.&quot;,
  157:     &quot;Life without you would be undefined.&quot;,
  158:     &quot;I&apos;ve calculated all outcomes — you&apos;re the best.&quot;,
  159:     &quot;Don&apos;t worry, I&apos;ve got this figured out to the last decimal.&quot;,
  160:     &quot;You&apos;re worth more than pi.&quot;,
  161:     &quot;I love you to infinity and beyond the number line.&quot;,
  162:     &quot;We&apos;ve got great chemistry — and the math checks out.&quot;,
  163:     &quot;Keep calm and carry the one.&quot;,
  164:     &quot;I&apos;m not obtuse — I know a good thing when I see it.&quot;,
  165:     &quot;You really raised my average.&quot;,
  166:     &quot;That idea has a lot of volume.&quot;,
  167:     &quot;Let&apos;s not make this a recurring problem.&quot;,
  168:     &quot;I&apos;ve got a good feeling about this — call it my gut instinct, or my gut-enberg theorem.&quot;,
  169:     &quot;You&apos;re never odd or even — you&apos;re just right.&quot;,
  170:     &quot;I could talk to you for an infinite series.&quot;,
  171:     &quot;Don&apos;t worry, this problem is well within your domain.&quot;,
  172:     &quot;You really know how to work a formula.&quot;,
  173:     &quot;I&apos;m totally rational — most of the time.&quot;,
  174:     &quot;Every angle of this plan looks solid.&quot;,
  175:     &quot;You&apos;re the denominator that keeps me grounded.&quot;,
  176:     &quot;My love for you has no upper bound.&quot;,
  177:     &quot;That was a calculated risk — totally worth it.&quot;,
  178:     &quot;You&apos;ve solved for the unknown in my heart.&quot;,
  179:     &quot;I&apos;m wholly devoted — no fractions about it.&quot;,
  180:     &quot;You&apos;re proof that good things come in natural numbers.&quot;,
  181:     &quot;The math is simple: me plus you equals everything.&quot;,
  182:     &quot;I&apos;d never want to find the limits of our friendship.&quot;,
  183:     &quot;You&apos;re an original — not a copy or a translation.&quot;,
  184:     &quot;We&apos;ve got great symmetry.&quot;,
  185:     &quot;I&apos;m not just winging it — I&apos;ve done the derivation.&quot;,
  186:     &quot;You had me at &apos;equals.&apos;&quot;,
  187:     &quot;Let&apos;s make today count — literally.&quot;,
  188:     &quot;This moment is statistically significant.&quot;,
  189:     &quot;You&apos;re the x to my y-axis.&quot;,
  190:     &quot;I&apos;m positively charged — must be the positive integers.&quot;,
  191:     &quot;We make a great set — no elements missing.&quot;,
  192:     &quot;Every problem has a solution when you&apos;re around.&quot;,
  193:     &quot;You&apos;re a real number — not imaginary at all.&quot;,
  194:     &quot;I mean this in the most geometric sense: you&apos;re well-rounded.&quot;,
  195:     &quot;Our bond is stronger than any postulate.&quot;,
  196:     &quot;I&apos;ve proven my hypothesis — you&apos;re amazing.&quot;,
  197:     &quot;You&apos;re complementary in every angle.&quot;,
  198:     &quot;Don&apos;t worry, I&apos;ve got the whole equation balanced.&quot;,
  199:     &quot;Life&apos;s a graph — and things are trending up.&quot;,
  200:     &quot;You&apos;re the right answer I didn&apos;t know I was solving for.&quot;,
  201:     &quot;I&apos;m fluent in the language of numbers — and you&apos;re my favorite expression.&quot;,
  202:     &quot;You&apos;ve got a great arc to your story.&quot;,
  203:     &quot;I never lose my train of thought — just my place in the sequence.&quot;,
  204:     &quot;You&apos;re the constant that makes my equation work.&quot;,
  205:     &quot;I&apos;ve run the numbers — this is a great idea.&quot;,
  206:     &quot;Nothing about you is average.&quot;,
  207:     &quot;You&apos;re divisible by awesome.&quot;,
  208:     &quot;I&apos;d walk a million units just to see you.&quot;,
  209:     &quot;You&apos;ve got the golden ratio of personality.&quot;,
  210:     &quot;This plan has a beautiful proof.&quot;,
  211:     &quot;I&apos;m not just theorizing — the data supports it.&quot;
  212: )
  213: 
  214: private fun nextPun(context: Context): String {
  215:     val prefs = context.getSharedPreferences(PUN_PREFS, Context.MODE_PRIVATE)
  216:     val lastPun = prefs.getString(LAST_PUN_KEY, null)
  217:     val nextPun = PUNS.filterNot { it == lastPun }.random()
  218:     prefs.edit().putString(LAST_PUN_KEY, nextPun).apply()
  219:     return nextPun
  220: }
  221: 
  222: // ---------------------------------------------------------------------------
  223: // Settings — sqrt mode
  224: // ---------------------------------------------------------------------------
  225: 
  226: private const val SETTINGS_PREFS = &quot;user_settings_prefs&quot;
  227: private const val SQRT_MODE_KEY = &quot;sqrt_mode&quot;
  228: private const val GESTURE_TYPING_KEY = &quot;gesture_typing_enabled&quot;
  229: private const val SHORTCUT_THRESHOLD_KEY = &quot;shortcut_threshold&quot;
  230: 
  231: private enum class SqrtMode {
  232:     WrapCurrentInput,
  233:     AppendToEnd,
  234:     AskEveryTime
  235: }
  236: 
  237: private fun loadGestureTypingEnabled(context: Context): Boolean {
  238:     val prefs = context.getSharedPreferences(CalculatorState.SETTINGS_PREFS, Context.MODE_PRIVATE)
  239:     return prefs.getBoolean(CalculatorState.GESTURE_TYPING_KEY, true)
  240: }
  241: 
  242: private fun saveGestureTypingEnabled(context: Context, enabled: Boolean) {
  243:     context.getSharedPreferences(CalculatorState.SETTINGS_PREFS, Context.MODE_PRIVATE)
  244:         .edit()
  245:         .putBoolean(CalculatorState.GESTURE_TYPING_KEY, enabled)
  246:         .apply()
  247: }
  248: 
  249: private fun loadShortcutThreshold(context: Context): Int {
  250:     val prefs = context.getSharedPreferences(CalculatorState.SETTINGS_PREFS, Context.MODE_PRIVATE)
  251:     return prefs.getInt(CalculatorState.SHORTCUT_THRESHOLD_KEY, 5)
  252: }
  253: 
  254: private fun saveShortcutThreshold(context: Context, threshold: Int) {
  255:     context.getSharedPreferences(CalculatorState.SETTINGS_PREFS, Context.MODE_PRIVATE)
  256:         .edit()
  257:         .putInt(CalculatorState.SHORTCUT_THRESHOLD_KEY, threshold)
  258:         .apply()
  259: }
  260: 
  261: private fun loadSqrtMode(context: Context): SqrtMode {
  262:     val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
  263:     return when (prefs.getString(SQRT_MODE_KEY, SqrtMode.AskEveryTime.name)) {
  264:         SqrtMode.WrapCurrentInput.name -&gt; SqrtMode.WrapCurrentInput
  265:         SqrtMode.AppendToEnd.name -&gt; SqrtMode.AppendToEnd
  266:         else -&gt; SqrtMode.AskEveryTime
  267:     }
  268: }
  269: 
  270: private fun saveSqrtMode(context: Context, mode: SqrtMode) {
  271:     context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
  272:         .edit()
  273:         .putString(SQRT_MODE_KEY, mode.name)
  274:         .apply()
  275: }
  276: 
  277: // ---------------------------------------------------------------------------
  278: // Settings — variables
  279: // ---------------------------------------------------------------------------
  280: 
  281: private const val VARS_PREFS = &quot;user_vars_prefs&quot;
  282: internal val VAR_NAMES = (1..10).map { &quot;v$it&quot; }           // &quot;v1&quot;..&quot;v10&quot;
  283: internal const val ANS_VAR = &quot;ans&quot;
  284: 
  285: // Tokens used in expressions, e.g. &quot;$var1&quot;, &quot;$ans&quot;
  286: internal fun varToken(name: String) = &quot;\$$name&quot;
  287: 
  288: private fun loadAllVariables(context: Context): MutableMap&lt;String, Double?&gt; {
  289:     val prefs = context.getSharedPreferences(VARS_PREFS, Context.MODE_PRIVATE)
  290:     val map = mutableMapOf&lt;String, Double?&gt;()
  291:     (VAR_NAMES + ANS_VAR).forEach { name -&gt;
  292:         map[name] = prefs.getString(name, null)?.toDoubleOrNull()
  293:     }
  294:     return map
  295: }
  296: 
  297: private fun persistVariable(context: Context, name: String, value: Double?) {
  298:     val editor = context.getSharedPreferences(VARS_PREFS, Context.MODE_PRIVATE).edit()
  299:     if (value == null) editor.remove(name) else editor.putString(name, value.toBigDecimal().toPlainString())
  300:     editor.apply()
  301: }
  302: 
  303: // ---------------------------------------------------------------------------
  304: // Settings — shortcuts
  305: // ---------------------------------------------------------------------------
  306: 
  307: private const val SHORTCUTS_PREFS = &quot;user_shortcuts_prefs&quot;
  308: 
  309: private fun loadShortcuts(context: Context): MutableMap&lt;String, String&gt; {
  310:     val prefs = context.getSharedPreferences(SHORTCUTS_PREFS, Context.MODE_PRIVATE)
  311:     val map = mutableMapOf&lt;String, String&gt;()
  312:     prefs.all.forEach { (key, value) -&gt;
  313:         if (value is String) map[key] = value
  314:     }
  315:     return map
  316: }
  317: 
  318: private fun persistShortcut(context: Context, name: String, value: String?) {
  319:     val editor = context.getSharedPreferences(SHORTCUTS_PREFS, Context.MODE_PRIVATE).edit()
  320:     if (value == null) editor.remove(name) else editor.putString(name, value)
  321:     editor.apply()
  322: }
  323: 
  324: // ---------------------------------------------------------------------------
  325: // Number formatting — shared top-level helper
  326: // ---------------------------------------------------------------------------
  327: 
  328: internal fun formatNumber(v: Double): String =
  329:     if (v == floor(v) &amp;&amp; !v.isInfinite()) v.toLong().toString()
  330:     else &quot;%.10g&quot;.format(v).trimEnd(&apos;0&apos;).trimEnd(&apos;.&apos;)
  331: 
  332: // ---------------------------------------------------------------------------
  333: // Tabs
  334: // ---------------------------------------------------------------------------
  335: 
  336: private enum class MainTab { Calculator, UserSettings }
  337: 
  338: // ---------------------------------------------------------------------------
  339: // Activity
  340: // ---------------------------------------------------------------------------
  341: 
  342: class MainActivity : ComponentActivity() {
  343:     override fun onCreate(savedInstanceState: Bundle?) {
  344:         super.onCreate(savedInstanceState)
  345:         window.setBackgroundDrawableResource(android.R.color.white)
  346:         // enableEdgeToEdge()
  347:         setContent {
  348:             CalculatorTheme {
  349:                 CalculatorScreen()
  350:             }
  351:         }
  352:     }
  353: }
  354: 
  355: // ---------------------------------------------------------------------------
  356: // ViewModel
  357: // ---------------------------------------------------------------------------
  358: 
  359: class CalculatorViewModel(app: Application) : AndroidViewModel(app) {
  360: 
  361:     private val ctx get() = getApplication&lt;Application&gt;()
  362:     private val historyManager = HistoryManager(ctx)
  363: 
  364:     // ── state ──────────────────────────────────────────────────────────────
  365:     var expression by mutableStateOf(&quot;&quot;)
  366:         private set
  367: 
  368:     var display by mutableStateOf(&quot;0&quot;)
  369:         private set
  370: 
  371:     val variables = mutableStateMapOf&lt;String, Double?&gt;()
  372:     val shortcuts = mutableStateMapOf&lt;String, String&gt;()
  373:     
  374:     var suggestedShortcut by mutableStateOf&lt;String?&gt;(null)
  375:         private set
  376: 
  377:     var shouldReplaceOnNextInput by mutableStateOf(false)
  378:         private set
  379:         
  380:     // For gesture typing
  381:     val buttonBounds = mutableStateMapOf&lt;String, androidx.compose.ui.geometry.Rect&gt;()
  382: 
  383:     /**
  384:      * Character offset in [expression] where the next insertion/deletion will happen.
  385:      * null  →  append-mode (default): new input goes to the end.
  386:      * non-null  →  cursor-mode: all input is inserted at this exact position.
  387:      *
  388:      * Set by [setCursor] when the user taps a display token.
  389:      */
  390:     var cursorPosition by mutableStateOf&lt;Int?&gt;(null)
  391:         private set
  392: 
  393:     /**
  394:      * The index of the display token whose END sits at [cursorPosition].
  395:      * Drives the green highlight in the UI.  Read-only computed value (not stored as state –
  396:      * Compose automatically tracks the mutableStateOf reads inside this getter).
  397:      */
  398:     var highlightedTokenIndex: Int? = null
  399:         get() {
  400:             val pos = cursorPosition ?: return null
  401:             val tokens = tokenizeForDisplay(expression)
  402:             if (tokens.isEmpty()) return null
  403:             var offset = 0
  404:             for ((idx, tok) in tokens.withIndex()) {
  405:                 offset += tok.length
  406:                 if (pos &lt;= offset) return idx
  407:             }
  408:             return tokens.lastIndex
  409:         }
  410: 
  411:     var previousExpression by mutableStateOf&lt;String?&gt;(null)
  412:         private set
  413:         
  414:     var isGestureTypingEnabled by mutableStateOf(true)
  415:     var shortcutThreshold by mutableStateOf(5)
  416: 
  417:     init {
  418:         variables.putAll(CalculatorState.loadAllVariables(ctx))
  419:         shortcuts.putAll(CalculatorState.loadShortcuts(ctx))
  420:         
  421:         isGestureTypingEnabled = loadGestureTypingEnabled(ctx)
  422:         shortcutThreshold = loadShortcutThreshold(ctx)
  423:     }
  424: 
  425:     // ── public helpers ─────────────────────────────────────────────────────
  426: 
  427:     fun setVariable(name: String, value: Double?) {
  428:         variables[name] = value
  429:         val editor = ctx.getSharedPreferences(CalculatorState.VARS_PREFS, Context.MODE_PRIVATE).edit()
  430:         if (value == null) editor.remove(name) else editor.putString(name, value.toBigDecimal().toPlainString())
  431:         editor.apply()
  432:     }
  433:     
  434:     fun removeVariable(name: String) {
  435:         variables.remove(name)
  436:         ctx.getSharedPreferences(CalculatorState.VARS_PREFS, Context.MODE_PRIVATE).edit().remove(name).apply()
  437:     }
  438: 
  439:     fun setShortcut(name: String, value: String?) {
  440:         if (value == null) {
  441:             shortcuts.remove(name)
  442:         } else {
  443:             shortcuts[name] = value
  444:         }
  445:         val editor = ctx.getSharedPreferences(CalculatorState.SHORTCUTS_PREFS, Context.MODE_PRIVATE).edit()
  446:         if (value == null) editor.remove(name) else editor.putString(name, value)
  447:         editor.apply()
  448: 
  449:         if (value != null &amp;&amp; suggestedShortcut == value) {
  450:             suggestedShortcut = null
  451:             historyManager.clearFrequency(value)
  452:         }
  453:     }
  454:     
  455:     fun dismissSuggestion() {
  456:         val suggested = suggestedShortcut ?: return
  457:         historyManager.clearFrequency(suggested)
  458:         suggestedShortcut = null
  459:     }
  460: 
  461:     /** Insert a variable token (e.g. &quot;$v1&quot;) into the expression. */
  462:     fun insertVariable(varName: String) {
  463:         val token = varToken(varName)
  464:         if (shouldReplaceOnNextInput) {
  465:             expression += token
  466:             shouldReplaceOnNextInput = false
  467:         } else {
  468:             expression += token
  469:         }
  470:         display = expression
  471:     }
  472:     
  473:     fun insertShortcut(name: String) {
  474:         val token = varToken(name)
  475:         if (shouldReplaceOnNextInput) {
  476:             expression += token
  477:             shouldReplaceOnNextInput = false
  478:         } else {
  479:             expression += token
  480:         }
  481:         display = expression
  482:     }
  483: 
  484:     /** Insert the assignment operator &quot; = &quot; into the expression. */
  485:     fun insertAssignEquals() {
  486:         expression += &quot; = &quot;
  487:         display = expression
  488:         shouldReplaceOnNextInput = false
  489:     }
  490: 
  491:     // ── key handler ────────────────────────────────────────────────────────
  492: 
  493:     fun onKey(key: String) {
  494:         // ── Cursor mode: insert / delete at the cursor offset ────────────────
  495:         val cur = cursorPosition
  496:         if (cur != null) {
  497:             handleKeyAtCursor(key, cur)
  498:             return
  499:         }
  500: 
  501:         when (key) {
  502:             &quot;C&quot; -&gt; {
  503:                 expression = &quot;&quot;
  504:                 display = &quot;0&quot;
  505:                 shouldReplaceOnNextInput = false
  506:                 previousExpression = null
  507:                 cursorPosition = null
  508:             }
  509: 
  510:             &quot;⌫&quot; -&gt; {
  511:                 if (expression.isNotEmpty()) {
  512:                     // Delete whole variable token if the expression ends with one
  513:                     val endingToken = knownVarTokens().firstOrNull { expression.endsWith(it) }
  514:                     expression = when {
  515:                         endingToken != null -&gt; expression.dropLast(endingToken.length)
  516:                         // Also delete &quot; = &quot; assignment operator as a unit
  517:                         expression.endsWith(&quot; = &quot;) -&gt; expression.dropLast(3)
  518:                         else -&gt; expression.dropLast(1)
  519:                     }
  520:                     display = if (expression.isEmpty()) &quot;0&quot; else expression
  521:                 }
  522:                 shouldReplaceOnNextInput = false
  523:             }
  524: 
  525:             &quot;=&quot; -&gt; {
  526:                 // ── Repeat: = pressed right after a previous result ───────
  527:                 if (shouldReplaceOnNextInput) {
  528:                     performRepeat()
  529:                     return
  530:                 }
  531: 
  532:                 val trimmed = expression.trim()
  533: 
  534:                 // ── Assignment: $varN = &lt;rhs&gt; ──────────────────────────────
  535:                 val assignMatch = Regex(&quot;&quot;&quot;^\$(\w+)\s*=\s*(.+)$&quot;&quot;&quot;).matchEntire(trimmed)
  536:                 if (assignMatch != null) {
  537:                     val varName = assignMatch.groupValues[1]
  538:                     val rhs = assignMatch.groupValues[2].trim()
  539:                     if (isValidVarName(varName)) {
  540:                         val result = safeEvaluate(trimTrailingOperator(rhs))
  541:                         if (result != &quot;Error&quot;) {
  542:                             setVariable(varName, result.toDoubleOrNull())
  543:                             display = &quot;${varToken(varName)} = $result&quot;
  544:                         } else {
  545:                             display = &quot;Error&quot;
  546:                         }
  547:                         expression = &quot;&quot;
  548:                         shouldReplaceOnNextInput = false
  549:                         return
  550:                     }
  551:                 }
  552: 
  553:                 // ── Normal evaluation ──────────────────────────────────────
  554:                 val exprToCapture = expression
  555:                 val result = safeEvaluate(trimTrailingOperator(expression))
  556:                 if (result != &quot;Error&quot;) {
  557:                     previousExpression = exprToCapture
  558:                     setVariable(ANS_VAR, result.toDoubleOrNull())
  559:                     
  560:                     // Frequency tracking for shortcuts
  561:                     val op = trimTrailingOperator(exprToCapture)
  562:                     if (op.length &gt; 2) { // Only track reasonably complex operations
  563:                         historyManager.incrementFrequency(op)
  564:                         val freq = historyManager.getFrequency(op)
  565:                         if (freq &gt;= shortcutThreshold &amp;&amp; !shortcuts.values.contains(op)) {
  566:                             suggestedShortcut = op
  567:                         }
  568:                     }
  569:                 }
  570:                 display = result
  571:                 expression = if (result == &quot;Error&quot;) &quot;&quot; else result
  572:                 shouldReplaceOnNextInput = result != &quot;Error&quot;
  573:                 cursorPosition = null
  574:             }
  575: 
  576:             &quot;+/-&quot; -&gt; {
  577:                 if (expression.isNotEmpty()) {
  578:                     expression = if (expression.startsWith(&quot;-&quot;)) expression.drop(1) else &quot;-$expression&quot;
  579:                     display = expression
  580:                 }
  581:                 shouldReplaceOnNextInput = false
  582:             }
  583: 
  584:             &quot;√&quot; -&gt; appendSqrtToEnd()
  585: 
  586:             &quot;%&quot; -&gt; {
  587:                 expression += &quot;%&quot;
  588:                 display = expression
  589:                 shouldReplaceOnNextInput = false
  590:             }
  591: 
  592:             &quot;xʸ&quot; -&gt; {
  593:                 appendOperator(&quot;^&quot;)
  594:                 display = expression
  595:                 shouldReplaceOnNextInput = false
  596:             }
  597: 
  598:             &quot;÷&quot; -&gt; { appendOperator(&quot;÷&quot;); display = expression; shouldReplaceOnNextInput = false }
  599:             &quot;×&quot; -&gt; { appendOperator(&quot;×&quot;); display = expression; shouldReplaceOnNextInput = false }
  600:             &quot;−&quot; -&gt; { appendOperator(&quot;−&quot;); display = expression; shouldReplaceOnNextInput = false }
  601:             &quot;+&quot; -&gt; { appendOperator(&quot;+&quot;); display = expression; shouldReplaceOnNextInput = false }
  602: 
  603:             else -&gt; {
  604:                 val isNumericStart = key.firstOrNull()?.isDigit() == true || key == &quot;.&quot;
  605:                 if (shouldReplaceOnNextInput) {
  606:                     if (isNumericStart) {
  607:                         expression = if (key == &quot;.&quot;) &quot;0.&quot; else key
  608:                         shouldReplaceOnNextInput = false
  609:                     } else if (key == &quot;(&quot;) {
  610:                         expression += &quot;(&quot;
  611:                         shouldReplaceOnNextInput = false
  612:                     } else {
  613:                         // Other keys (like &quot;)&quot; or unknown) reset to ensure clean state
  614:                         expression = key
  615:                         shouldReplaceOnNextInput = false
  616:                     }
  617:                 } else {
  618:                     if (key == &quot;.&quot; &amp;&amp; (expression.isEmpty() || expression.last() in setOf(&apos;+&apos;, &apos;-&apos;, &apos;×&apos;, &apos;÷&apos;, &apos;^&apos;))) {
  619:                         expression += &quot;0&quot;
  620:                     }
  621:                     expression += key
  622:                 }
  623:                 display = expression
  624:             }
  625:         }
  626:     }
  627: 
  628:     fun applySqrtWrap() {
  629:         val base = trimTrailingOperator(expression)
  630:         expression = if (base.isEmpty()) &quot;√&quot; else &quot;√($base)&quot;
  631:         display = expression
  632:         shouldReplaceOnNextInput = false
  633:     }
  634: 
  635:     fun appendSqrtToEnd() {
  636:         if (shouldReplaceOnNextInput) {
  637:             shouldReplaceOnNextInput = false
  638:         }
  639:         expression += &quot;√&quot;
  640:         display = expression
  641:     }
  642: 
  643:     /** Load the previous expression back into the input without evaluating it. */
  644:     fun loadPreviousExpression() {
  645:         val prev = previousExpression ?: return
  646:         expression = prev
  647:         display = prev
  648:         shouldReplaceOnNextInput = false
  649:         cursorPosition = null
  650:     }
  651: 
  652:     // ── token display &amp; cursor helpers ─────────────────────────────────────
  653: 
  654:     /**
  655:      * Splits [expr] into display tokens, preserving original Unicode operators.
  656:      * Numbers (consecutive digit/dot runs) are grouped into a single token.
  657:      * Variable tokens ($ans, $var1 …) are kept whole.
  658:      * Shortcut tokens are also kept whole.
  659:      * Every other character becomes its own single-char token.
  660:      */
  661:     fun tokenizeForDisplay(expr: String = expression): List&lt;String&gt; {
  662:         if (expr.isEmpty()) return emptyList()
  663:         val result = mutableListOf&lt;String&gt;()
  664:         var i = 0
  665:         while (i &lt; expr.length) {
  666:             when {
  667:                 expr[i] == &apos;$&apos; -&gt; {
  668:                     val sb = StringBuilder(&quot;\$&quot;)
  669:                     i++
  670:                     while (i &lt; expr.length &amp;&amp; (expr[i].isLetter() || expr[i].isDigit())) sb.append(expr[i++])
  671:                     result += sb.toString()
  672:                 }
  673:                 expr[i].isDigit() || expr[i] == &apos;.&apos; -&gt; {
  674:                     val sb = StringBuilder()
  675:                     while (i &lt; expr.length &amp;&amp; (expr[i].isDigit() || expr[i] == &apos;.&apos;)) sb.append(expr[i++])
  676:                     result += sb.toString()
  677:                 }
  678:                 else -&gt; { result += expr[i].toString(); i++ }
  679:             }
  680:         }
  681:         return result
  682:     }
  683: 
  684:     /**
  685:      * Places the cursor at the END of the token at [tokenIndex] in the expression string,
  686:      * enabling cursor-mode.  Tapping the already-highlighted token toggles back to append mode.
  687:      * Pass null to force append mode.
  688:      */
  689:     fun setCursor(tokenIndex: Int?) {
  690:         if (tokenIndex == null) { cursorPosition = null; return }
  691:         val tokens = tokenizeForDisplay(expression)
  692:         if (tokenIndex &gt;= tokens.size) { cursorPosition = null; return }
  693:         val newPos = tokens.take(tokenIndex + 1).sumOf { it.length }
  694:         cursorPosition = if (cursorPosition == newPos) null else newPos   // toggle
  695:     }
  696: 
  697:     /**
  698:      * Handles any button press while [cursorPosition] is non-null.
  699:      *
  700:      * Model: pure string insertion / deletion at the cursor character offset — no smart
  701:      * operator-replacement or token-awareness.  Every key inserts its characters literally
  702:      * at the current position and advances the cursor by that many characters.
  703:      *
  704:      * The only exceptions are lifecycle keys (C, =, +/-) and ⌫.
  705:      */
  706:     private fun handleKeyAtCursor(key: String, cur: Int) {
  707:         when (key) {
  708:             &quot;C&quot; -&gt; {
  709:                 expression = &quot;&quot;; display = &quot;0&quot;
  710:                 shouldReplaceOnNextInput = false; previousExpression = null
  711:                 cursorPosition = null; return
  712:             }
  713:             &quot;=&quot; -&gt; { cursorPosition = null; onKey(&quot;=&quot;); return }
  714:             &quot;+/-&quot; -&gt; {
  715:                 // No clean cursor-aware sign toggle — fall back to global behaviour.
  716:                 cursorPosition = null; onKey(&quot;+/-&quot;); return
  717:             }
  718:             &quot;⌫&quot; -&gt; {
  719:                 if (cur == 0) { cursorPosition = null; return }
  720:                 val before = expression.substring(0, cur)
  721:                 val varTok = knownVarTokens().firstOrNull { before.endsWith(it) }
  722:                 val deleteLen = when {
  723:                     varTok != null          -&gt; varTok.length
  724:                     before.endsWith(&quot; = &quot;) -&gt; 3
  725:                     else                   -&gt; 1
  726:                 }
  727:                 expression = expression.substring(0, cur - deleteLen) + expression.substring(cur)
  728:                 cursorPosition = (cur - deleteLen).coerceAtLeast(0)
  729:                 display = if (expression.isEmpty()) &quot;0&quot; else expression
  730:                 return
  731:             }
  732:             else -&gt; {
  733:                 val insert = when (key) {
  734:                     &quot;÷&quot; -&gt; &quot;÷&quot;; &quot;×&quot; -&gt; &quot;×&quot;; &quot;−&quot; -&gt; &quot;−&quot;; &quot;+&quot; -&gt; &quot;+&quot;
  735:                     &quot;xʸ&quot; -&gt; &quot;^&quot;; &quot;√&quot; -&gt; &quot;√&quot;; &quot;%&quot; -&gt; &quot;%&quot;
  736:                     &quot;(&quot; -&gt; &quot;(&quot;; &quot;)&quot; -&gt; &quot;)&quot;
  737:                     else -&gt; key
  738:                 }
  739:                 expression = expression.substring(0, cur) + insert + expression.substring(cur)
  740:                 cursorPosition = cur + insert.length
  741:                 display = expression
  742:             }
  743:         }
  744:     }
  745: 
  746:     // ── private helpers ────────────────────────────────────────────────────
  747: 
  748:     private fun isValidVarName(name: String): Boolean {
  749:         if (name == ANS_VAR) return true
  750:         val n = name.removePrefix(&quot;v&quot;).toIntOrNull() ?: return false
  751:         return n in 1..10
  752:     }
  753: 
  754:     /** All known variable tokens sorted longest-first for correct endsWith checks. */
  755:     private fun knownVarTokens(): List&lt;String&gt; =
  756:         (VAR_NAMES + ANS_VAR).map { varToken(it) }.sortedByDescending { it.length }
  757: 
  758:     private fun appendOperator(op: String) {
  759:         if (expression.isEmpty() &amp;&amp; op != &quot;-&quot;) return
  760:         if (expression.isNotEmpty() &amp;&amp; expression.last() in setOf(&apos;+&apos;, &apos;-&apos;, &apos;×&apos;, &apos;÷&apos;, &apos;^&apos;)) {
  761:             expression = expression.dropLast(1)
  762:         }
  763:         expression += op
  764:     }
  765: 
  766:     private fun trimTrailingOperator(value: String): String =
  767:         value.trimEnd { it in setOf(&apos;+&apos;, &apos;-&apos;, &apos;×&apos;, &apos;÷&apos;, &apos;^&apos;) }
  768: 
  769:     // ── repeat helpers ─────────────────────────────────────────────────────
  770: 
  771:     private fun performRepeat() {
  772:         val ansValue = variables[ANS_VAR] ?: return
  773:         val ansStr = CalculatorEngine.formatNumber(ansValue)
  774: 
  775:         // The previous expression that led to the current &apos;ans&apos;
  776:         val prevExpr = previousExpression ?: return
  777:         
  778:         // Find the operator and the operand from the previous expression
  779:         // e.g., if prevExpr was &quot;10 + 5&quot;, and ans is 15, we want to do &quot;15 + 5&quot;
  780:         val match = Regex(&quot;&quot;&quot;([+\-×÷^])\s*(\d*\.?\d+)$&quot;&quot;&quot;).find(prevExpr.trim())
  781:         val newExpr = if (match != null) {
  782:             val op = match.groupValues[1]
  783:             val operand = match.groupValues[2]
  784:             ansStr + op + operand
  785:         } else {
  786:             // Fallback: try to substitute first number if it was a simple repeat before
  787:             trySubstituteFirstNumber(expression, ansStr) ?: expression
  788:         }
  789: 
  790:         val result = safeEvaluate(trimTrailingOperator(newExpr))
  791:         if (result != &quot;Error&quot;) {
  792:             setVariable(ANS_VAR, result.toDoubleOrNull())
  793:         }
  794:         display = result
  795:         expression = if (result == &quot;Error&quot;) newExpr else result
  796:         shouldReplaceOnNextInput = result != &quot;Error&quot;
  797:     }
  798: 
  799:     private fun trySubstituteFirstNumber(expr: String, replacement: String): String? {
  800:         val match = Regex(&quot;&quot;&quot;^\d*\.?\d+&quot;&quot;&quot;).find(expr) ?: return null
  801:         val after = expr.substring(match.range.last + 1).trim()
  802:         if (after.isEmpty() || !after.first().let { it in &quot;+-×÷^&quot; }) return null
  803:         
  804:         return replacement + expr.substring(match.range.last + 1)
  805:     }
  806: 
  807:     private fun safeEvaluate(expr: String): String {
  808:         return CalculatorEngine.safeEvaluate(expr, variables, shortcuts)
  809:     }
  810: }
  811: 
  812: // ---------------------------------------------------------------------------
  813: // Root screen
  814: // ---------------------------------------------------------------------------
  815: 
  816: @Composable
  817: fun CalculatorScreen(vm: CalculatorViewModel = viewModel()) {
  818:     val context = LocalContext.current
  819:     val launcherPun = remember { nextPun(context) }
  820:     var selectedTab by remember { mutableStateOf(MainTab.Calculator) }
  821:     var sqrtMode by remember { mutableStateOf(loadSqrtMode(context)) }
  822:     var pendingSqrtDialog by remember { mutableStateOf(false) }
  823: 
  824:     if (pendingSqrtDialog) {
  825:         AlertDialog(
  826:             onDismissRequest = { pendingSqrtDialog = false },
  827:             title = { Text(&quot;Square root&quot;) },
  828:             text = { Text(&quot;How should √ behave right now?&quot;) },
  829:             confirmButton = {
  830:                 TextButton(onClick = { vm.applySqrtWrap(); pendingSqrtDialog = false }) {
  831:                     Text(&quot;Wrap current input&quot;)
  832:                 }
  833:             },
  834:             dismissButton = {
  835:                 TextButton(onClick = { vm.appendSqrtToEnd(); pendingSqrtDialog = false }) {
  836:                     Text(&quot;Add to end&quot;)
  837:                 }
  838:             }
  839:         )
  840:     }
  841: 
  842:     Column(
  843:         modifier = Modifier
  844:             .fillMaxSize()
  845:             .background(AppWhite)
  846:             .systemBarsPadding()
  847:             .padding(horizontal = 20.dp, vertical = 18.dp),
  848:         verticalArrangement = Arrangement.spacedBy(16.dp)
  849:     ) {
  850:         // ── Header ─────────────────────────────────────────────────────────
  851:         Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
  852:             Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
  853:                 Text(
  854:                     text = launcherPun,
  855:                     color = AppGreenDeep,
  856:                     fontSize = 13.sp,
  857:                     fontWeight = FontWeight.SemiBold
  858:                 )
  859:                 Text(
  860:                     text = if (selectedTab == MainTab.Calculator) &quot;Calculator&quot; else &quot;User settings&quot;,
  861:                     color = AppBlack,
  862:                     fontSize = 22.sp,
  863:                     fontWeight = FontWeight.Bold
  864:                 )
  865:             }
  866:             IconButton(onClick = {
  867:                 selectedTab = if (selectedTab == MainTab.Calculator) MainTab.UserSettings else MainTab.Calculator
  868:             }) {
  869:                 Icon(
  870:                     imageVector = Icons.Filled.Settings,
  871:                     contentDescription = &quot;Open settings&quot;,
  872:                     tint = AppBlack
  873:                 )
  874:             }
  875:         }
  876: 
  877:         // ── Previous expression ── Outside scrollable elements
  878:         val prevExpr = vm.previousExpression
  879:         if (prevExpr != null &amp;&amp; selectedTab == MainTab.Calculator) {
  880:             Text(
  881:                 text = prevExpr,
  882:                 modifier = Modifier
  883:                     .fillMaxWidth()
  884:                     .clickable(
  885:                         indication = null,
  886:                         interactionSource = remember { MutableInteractionSource() }
  887:                     ) { vm.loadPreviousExpression() }
  888:                     .padding(horizontal = 4.dp),
  889:                 color = AppGrayMuted,
  890:                 fontSize = 14.sp,
  891:                 fontWeight = FontWeight.Medium,
  892:                 textAlign = TextAlign.End,
  893:                 maxLines = 1,
  894:                 overflow = TextOverflow.Ellipsis
  895:             )
  896:         }
  897: 
  898:         // ── Shortcut Suggestion ────────────────────────────────────────────
  899:         val suggestion = vm.suggestedShortcut
  900:         if (suggestion != null) {
  901:             ShortcutSuggestionBanner(
  902:                 suggestion = suggestion,
  903:                 onAccept = { name -&gt; vm.setShortcut(name, suggestion) },
  904:                 onDismiss = { vm.dismissSuggestion() }
  905:             )
  906:         }
  907: 
  908:         // ── Tab content ────────────────────────────────────────────────────
  909:         when (selectedTab) {
  910:             MainTab.Calculator -&gt; CalculatorTabContent(
  911:                 vm = vm,
  912:                 onSqrtClick = {
  913:                     when (sqrtMode) {
  914:                         SqrtMode.WrapCurrentInput -&gt; vm.applySqrtWrap()
  915:                         SqrtMode.AppendToEnd -&gt; vm.appendSqrtToEnd()
  916:                         SqrtMode.AskEveryTime -&gt; pendingSqrtDialog = true
  917:                     }
  918:                 }
  919:             )
  920:             MainTab.UserSettings -&gt; UserSettingsTabContent(
  921:                 currentSqrtMode = sqrtMode,
  922:                 onModeSelected = { sqrtMode = it; saveSqrtMode(context, it) },
  923:                 variables = vm.variables,
  924:                 onVariableSet = { name, value -&gt; vm.setVariable(name, value) },
  925:                 vm = vm
  926:             )
  927:         }
  928:     }
  929: }
  930: 
  931: // ---------------------------------------------------------------------------
  932: // Calculator tab
  933: // ---------------------------------------------------------------------------
  934: 
  935: private data class CalcKey(
  936:     val label: String,
  937:     val style: KeyStyle = KeyStyle.Default,
  938:     val widthWeight: Float = 1f
  939: )
  940: 
  941: private enum class KeyStyle { Default, Digit, Utility, Operation, Equal }
  942: 
  943: @Composable
  944: private fun ColumnScope.CalculatorTabContent(
  945:     vm: CalculatorViewModel,
  946:     onSqrtClick: () -&gt; Unit
  947: ) {
  948:     Column(
  949:         modifier = Modifier
  950:             .fillMaxWidth()
  951:             .weight(1f)
  952:             .clip(RoundedCornerShape(28.dp))
  953:             .background(AppGray)
  954:             .border(1.dp, AppGrayBorder, RoundedCornerShape(28.dp))
  955:             .padding(18.dp),
  956:         verticalArrangement = Arrangement.spacedBy(14.dp)
  957:     ) {
  958:         // ── Display ────────────────────────────────────────────────────────
  959:         Box(
  960:             modifier = Modifier
  961:                 .fillMaxWidth()
  962:                 .height(200.dp)
  963:                 .clip(RoundedCornerShape(22.dp))
  964:                 .background(AppWhite)
  965:                 .border(1.dp, AppGrayBorder, RoundedCornerShape(22.dp))
  966:                 // Tapping empty space in the display → deselect (back to append mode)
  967:                 .clickable(
  968:                     indication = null,
  969:                     interactionSource = remember { MutableInteractionSource() }
  970:                 ) { vm.setCursor(null) }
  971:                 .padding(18.dp),
  972:             contentAlignment = Alignment.BottomEnd
  973:         ) {
  974:             Column(
  975:                 modifier = Modifier
  976:                     .fillMaxSize()
  977:                     .verticalScroll(rememberScrollState())
  978:             ) {
  979:                 Spacer(Modifier.height(16.dp))
  980: 
  981:                 // ── Bottom area: interactive token row  OR  plain result ───
  982:                 val showTokens = vm.expression.isNotEmpty() &amp;&amp; !vm.shouldReplaceOnNextInput
  983:                 if (showTokens) {
  984:                     ExpressionTokenRow(
  985:                         tokens = vm.tokenizeForDisplay(),
  986:                         selectedIndex = vm.highlightedTokenIndex,
  987:                         onTokenTap = { vm.setCursor(it) },
  988:                         modifier = Modifier.fillMaxWidth()
  989:                     )
  990:                     if (vm.cursorPosition != null) {
  991:                         Spacer(Modifier.height(4.dp))
  992:                         Text(
  993:                             text = &quot;tap display to exit token edit&quot;,
  994:                             color = AppGreen.copy(alpha = 0.55f),
  995:                             fontSize = 11.sp,
  996:                             textAlign = TextAlign.End,
  997:                             modifier = Modifier.fillMaxWidth()
  998:                         )
  999:                     }
  1000:                 } else {
  1001:                     AnimatedContent(
  1002:                         targetState = vm.display,
  1003:                         label = &quot;displayTransition&quot;,
  1004:                         transitionSpec = {
  1005:                             fadeIn(animationSpec = tween(120)) togetherWith fadeOut(animationSpec = tween(90))
  1006:                         }
  1007:                     ) { displayValue -&gt;
  1008:                         Text(
  1009:                             text = displayValue,
  1010:                             color = AppBlack,
  1011:                             fontSize = 36.sp,
  1012:                             fontWeight = FontWeight.Bold,
  1013:                             textAlign = TextAlign.End,
  1014:                             softWrap = true,
  1015:                             modifier = Modifier.fillMaxWidth()
  1016:                         )
  1017:                     }
  1018:                 }
  1019:             }
  1020:         }
  1021: 
  1022:         // ── Variable chip row ──────────────────────────────────────────────
  1023:         VariableChipRow(vm = vm)
  1024: 
  1025:         // ── Buttons ────────────────────────────────────────────────────────
  1026:         var containerPosition by remember { mutableStateOf(Offset.Zero) }
  1027:         Box(
  1028:             modifier = Modifier
  1029:                 .fillMaxWidth()
  1030:                 .weight(1f)
  1031:                 .onGloballyPositioned { containerPosition = it.positionInWindow() }
  1032:                 .pointerInput(vm.isGestureTypingEnabled) {
  1033:                     if (!vm.isGestureTypingEnabled) return@pointerInput
  1034:                     var lastButton: String? = null
  1035:                     var lastTime = 0L
  1036:                     
  1037:                     detectDragGestures(
  1038:                         onDragStart = { lastButton = null; lastTime = System.currentTimeMillis() },
  1039:                         onDragEnd = { lastButton = null },
  1040:                         onDragCancel = { lastButton = null },
  1041:                         onDrag = { change, _ -&gt;
  1042:                             val currentTime = System.currentTimeMillis()
  1043:                             val windowPos = containerPosition + change.position
  1044:                             val hit = vm.buttonBounds.entries.find { it.value.contains(windowPos) }
  1045:                             
  1046:                             if (hit != null) {
  1047:                                 if (hit.key != lastButton) {
  1048:                                     // It&apos;s a new button. 
  1049:                                     // To simulate &quot;holding pressure&quot;, we only register if moving slow enough?
  1050:                                     // Or better: the user must &quot;pause&quot; slightly on a key.
  1051:                                     // Actually, &quot;pressure&quot; in terms of touch is not reliable on all screens.
  1052:                                     // We will use velocity or time-spent.
  1053:                                     
  1054:                                     // If swipe is FAST, don&apos;t register.
  1055:                                     val velocity = change.positionChange().getDistance() / (currentTime - lastTime).coerceAtLeast(1)
  1056:                                     if (velocity &lt; 0.5f) { // threshold for &quot;intent&quot; vs &quot;swipe-over&quot;
  1057:                                          vm.onKey(hit.key)
  1058:                                          lastButton = hit.key
  1059:                                     }
  1060:                                 }
  1061:                             } else {
  1062:                                 lastButton = null
  1063:                             }
  1064:                             lastTime = currentTime
  1065:                         }
  1066:                     )
  1067:                 }
  1068:         ) {
  1069:             Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
  1070:                 ButtonRow(
  1071:                     listOf(
  1072:                         CalcKey(&quot;C&quot;, KeyStyle.Utility), CalcKey(&quot;⌫&quot;, KeyStyle.Utility),
  1073:                         CalcKey(&quot;%&quot;, KeyStyle.Utility), CalcKey(&quot;√&quot;, KeyStyle.Utility)
  1074:                     ),
  1075:                     vm, onSqrtClick = onSqrtClick
  1076:                 )
  1077:                 ButtonRow(listOf(CalcKey(&quot;7&quot;), CalcKey(&quot;8&quot;), CalcKey(&quot;9&quot;), CalcKey(&quot;÷&quot;, KeyStyle.Operation)), vm)
  1078:                 ButtonRow(listOf(CalcKey(&quot;4&quot;), CalcKey(&quot;5&quot;), CalcKey(&quot;6&quot;), CalcKey(&quot;×&quot;, KeyStyle.Operation)), vm)
  1079:                 ButtonRow(
  1080:                     listOf(CalcKey(&quot;1&quot;), CalcKey(&quot;2&quot;), CalcKey(&quot;3&quot;)), vm,
  1081:                     operatorKey = CalcKey(&quot;(&quot;, KeyStyle.Operation),
  1082:                     extraKey = CalcKey(&quot;−&quot;, KeyStyle.Operation)
  1083:                 )
  1084:                 ButtonRow(
  1085:                     listOf(CalcKey(&quot;0&quot;, widthWeight = 2f), CalcKey(&quot;.&quot;)), vm,
  1086:                     operatorKey = CalcKey(&quot;)&quot;, KeyStyle.Operation),
  1087:                     extraKey = CalcKey(&quot;+&quot;, KeyStyle.Operation)
  1088:                 )
  1089: 
  1090:                 Row(
  1091:                     horizontalArrangement = Arrangement.spacedBy(10.dp),
  1092:                     modifier = Modifier.fillMaxWidth()
  1093:                 ) {
  1094:                     CalculatorButton(
  1095:                         text = &quot;xʸ&quot;, style = KeyStyle.Operation,
  1096:                         modifier = Modifier.weight(1f).height(60.dp),
  1097:                         onClick = { vm.onKey(&quot;xʸ&quot;) }
  1098:                     )
  1099:                     CalculatorButton(
  1100:                         text = &quot;=&quot;, style = KeyStyle.Equal,
  1101:                         modifier = Modifier.weight(2f).height(60.dp),
  1102:                         onClick = { vm.onKey(&quot;=&quot;) }
  1103:                     )
  1104:                 }
  1105:             }
  1106:         }
  1107:     }
  1108: }
  1109: 
  1110: @Composable
  1111: private fun ShortcutSuggestionBanner(
  1112:     suggestion: String,
  1113:     onAccept: (String) -&gt; Unit,
  1114:     onDismiss: () -&gt; Unit
  1115: ) {
  1116:     var shortcutName by remember { mutableStateOf(&quot;&quot;) }
  1117:     
  1118:     Column(
  1119:         modifier = Modifier
  1120:             .fillMaxWidth()
  1121:             .clip(RoundedCornerShape(16.dp))
  1122:             .background(AppGreenSoft)
  1123:             .border(1.dp, AppGreen, RoundedCornerShape(16.dp))
  1124:             .padding(16.dp),
  1125:         verticalArrangement = Arrangement.spacedBy(8.dp)
  1126:     ) {
  1127:         Text(
  1128:             text = &quot;Hey, it seems that you used this equation 20 times, do you want to create a shortcut for that?&quot;,
  1129:             color = AppGreenDeep,
  1130:             fontSize = 14.sp,
  1131:             fontWeight = FontWeight.Medium
  1132:         )
  1133:         Text(
  1134:             text = suggestion,
  1135:             color = AppBlack,
  1136:             fontSize = 16.sp,
  1137:             fontWeight = FontWeight.Bold
  1138:         )
  1139:         Row(
  1140:             modifier = Modifier.fillMaxWidth(),
  1141:             horizontalArrangement = Arrangement.spacedBy(8.dp),
  1142:             verticalAlignment = Alignment.CenterVertically
  1143:         ) {
  1144:             OutlinedTextField(
  1145:                 value = shortcutName,
  1146:                 onValueChange = { shortcutName = it },
  1147:                 modifier = Modifier.weight(1f),
  1148:                 placeholder = { Text(&quot;Shortcut name (e.g. VAT)&quot;) },
  1149:                 singleLine = true,
  1150:                 colors = OutlinedTextFieldDefaults.colors(
  1151:                     focusedBorderColor = AppGreen,
  1152:                     unfocusedBorderColor = AppGreen.copy(alpha = 0.5f)
  1153:                 )
  1154:             )
  1155:             TextButton(onClick = { if (shortcutName.isNotBlank()) onAccept(shortcutName) }) {
  1156:                 Text(&quot;Create&quot;, color = AppGreenDeep, fontWeight = FontWeight.Bold)
  1157:             }
  1158:             TextButton(onClick = onDismiss) {
  1159:                 Text(&quot;Dismiss&quot;, color = AppGrayMuted)
  1160:             }
  1161:         }
  1162:     }
  1163: }
  1164: 
  1165: // ---------------------------------------------------------------------------
  1166: // Variable chip row
  1167: // ---------------------------------------------------------------------------
  1168: 
  1169: @Composable
  1170: private fun VariableChipRow(vm: CalculatorViewModel) {
  1171:     Row(
  1172:         modifier = Modifier
  1173:             .fillMaxWidth()
  1174:             .horizontalScroll(rememberScrollState()),
  1175:         horizontalArrangement = Arrangement.spacedBy(6.dp),
  1176:         verticalAlignment = Alignment.CenterVertically
  1177:     ) {
  1178:         // &quot; = &quot; assignment operator chip
  1179:         AssignEqualsChip(onClick = { vm.insertAssignEquals() })
  1180: 
  1181:         // $ans
  1182:         val ansValue = vm.variables[ANS_VAR]
  1183:         VariableChip(
  1184:             chipLabel = &quot;\$ans&quot;,
  1185:             valueLabel = if (ansValue != null) formatNumber(ansValue) else &quot;—&quot;,
  1186:             isAns = true,
  1187:             onClick = { vm.insertVariable(ANS_VAR) }
  1188:         )
  1189: 
  1190:         // Existing variables
  1191:         vm.variables.keys.filter { it != ANS_VAR }.sortedBy { it.removePrefix(&quot;v&quot;).toIntOrNull() ?: 99 }.forEach { name -&gt;
  1192:             val value = vm.variables[name]
  1193:             VariableChip(
  1194:                 chipLabel = &quot;\$$name&quot;,
  1195:                 valueLabel = if (value != null) formatNumber(value) else &quot;—&quot;,
  1196:                 isAns = false,
  1197:                 onClick = { vm.insertVariable(name) }
  1198:             )
  1199:         }
  1200:         
  1201:         // Shortcuts
  1202:         vm.shortcuts.forEach { (name, value) -&gt;
  1203:             VariableChip(
  1204:                 chipLabel = &quot;\$$name&quot;,
  1205:                 valueLabel = value,
  1206:                 isAns = false,
  1207:                 onClick = { vm.insertShortcut(name) }
  1208:             )
  1209:         }
  1210:     }
  1211: }
  1212: 
  1213: @Composable
  1214: private fun VariableChip(
  1215:     chipLabel: String,
  1216:     valueLabel: String,
  1217:     isAns: Boolean,
  1218:     onClick: () -&gt; Unit
  1219: ) {
  1220:     val bg = if (isAns) AppGreenSoft else AppWhite
  1221:     val border = if (isAns) AppGreen else AppGrayBorder
  1222: 
  1223:     Column(
  1224:         modifier = Modifier
  1225:             .wrapContentWidth()
  1226:             .clip(RoundedCornerShape(12.dp))
  1227:             .background(bg)
  1228:             .border(1.dp, border, RoundedCornerShape(12.dp))
  1229:             .clickable(
  1230:                 indication = null,
  1231:                 interactionSource = remember { MutableInteractionSource() },
  1232:                 onClick = onClick
  1233:             )
  1234:             .padding(horizontal = 10.dp, vertical = 5.dp),
  1235:         horizontalAlignment = Alignment.CenterHorizontally,
  1236:         verticalArrangement = Arrangement.spacedBy(1.dp)
  1237:     ) {
  1238:         Text(
  1239:             text = chipLabel,
  1240:             color = if (isAns) AppGreenDeep else AppBlack,
  1241:             fontSize = 12.sp,
  1242:             fontWeight = FontWeight.Bold,
  1243:             textAlign = TextAlign.Center
  1244:         )
  1245:         Text(
  1246:             text = valueLabel,
  1247:             color = if (isAns) AppGreenDeep else AppGrayMuted,
  1248:             fontSize = 10.sp,
  1249:             fontWeight = FontWeight.Medium,
  1250:             textAlign = TextAlign.Center,
  1251:             maxLines = 1,
  1252:             overflow = TextOverflow.Ellipsis
  1253:         )
  1254:     }
  1255: }
  1256: 
  1257: @Composable
  1258: private fun AssignEqualsChip(onClick: () -&gt; Unit) {
  1259:     Box(
  1260:         modifier = Modifier
  1261:             .clip(RoundedCornerShape(12.dp))
  1262:             .background(AppGreen)
  1263:             .border(1.dp, AppGreenDeep, RoundedCornerShape(12.dp))
  1264:             .clickable(
  1265:                 indication = null,
  1266:                 interactionSource = remember { MutableInteractionSource() },
  1267:                 onClick = onClick
  1268:             )
  1269:             .padding(horizontal = 12.dp, vertical = 5.dp),
  1270:         contentAlignment = Alignment.Center
  1271:     ) {
  1272:         Text(
  1273:             text = &quot;=&quot;,
  1274:             color = AppWhite,
  1275:             fontSize = 16.sp,
  1276:             fontWeight = FontWeight.Bold,
  1277:             textAlign = TextAlign.Center
  1278:         )
  1279:     }
  1280: }
  1281: 
  1282: // ---------------------------------------------------------------------------
  1283: // Expression token row (interactive expression display)
  1284: // ---------------------------------------------------------------------------
  1285: 
  1286: /**
  1287:  * Renders [tokens] as a horizontally-scrollable row of individually tappable chips.
  1288:  *
  1289:  * • The selected chip (matching [selectedIndex]) is highlighted in green.
  1290:  * • Tapping a chip fires [onTokenTap] with the chip&apos;s index.
  1291:  * • Tapping the already-selected chip calls [onTokenTap] with the same index,
  1292:  *   which the ViewModel interprets as a toggle → deselect.
  1293:  *
  1294:  * The row auto-scrolls to the right-most token whenever the expression grows
  1295:  * (append-mode), and to the selected token when one is active.
  1296:  */
  1297: @Composable
  1298: private fun ExpressionTokenRow(
  1299:     tokens: List&lt;String&gt;,
  1300:     selectedIndex: Int?,
  1301:     onTokenTap: (Int) -&gt; Unit,
  1302:     modifier: Modifier = Modifier
  1303: ) {
  1304:     val scrollState = rememberScrollState()
  1305: 
  1306:     // Auto-scroll: stay at the end in append mode; don&apos;t jump when cursor is active.
  1307:     LaunchedEffect(tokens.size, selectedIndex) {
  1308:         if (selectedIndex == null) {
  1309:             scrollState.animateScrollTo(scrollState.maxValue)
  1310:         }
  1311:     }
  1312: 
  1313:     Row(
  1314:         modifier = modifier
  1315:             .horizontalScroll(scrollState),
  1316:         horizontalArrangement = Arrangement.End,
  1317:         verticalAlignment = Alignment.CenterVertically
  1318:     ) {
  1319:         tokens.forEachIndexed { index, token -&gt;
  1320:             val isSelected = index == selectedIndex
  1321:             // Skip plain space tokens visually (they keep spacing via the row arrangement)
  1322:             val displayText = if (token == &quot; &quot;) &quot;\u2009&quot; else token   // thin-space placeholder
  1323: 
  1324:             Box(
  1325:                 modifier = Modifier
  1326:                     .clip(RoundedCornerShape(8.dp))
  1327:                     .background(if (isSelected) AppGreenSoft else Color.Transparent)
  1328:                     .then(
  1329:                         if (isSelected)
  1330:                             Modifier.border(1.dp, AppGreen, RoundedCornerShape(8.dp))
  1331:                         else
  1332:                             Modifier
  1333:                     )
  1334:                     .clickable(
  1335:                         indication = null,
  1336:                         interactionSource = remember(index) { MutableInteractionSource() }
  1337:                     ) { onTokenTap(index) }
  1338:                     .padding(horizontal = 2.dp, vertical = 1.dp),
  1339:                 contentAlignment = Alignment.Center
  1340:             ) {
  1341:                 Text(
  1342:                     text = displayText,
  1343:                     color = if (isSelected) AppGreenDeep else AppBlack,
  1344:                     fontSize = 26.sp,
  1345:                     fontWeight = FontWeight.Bold,
  1346:                     textAlign = TextAlign.Center
  1347:                 )
  1348:             }
  1349:         }
  1350:     }
  1351: }
  1352: 
  1353: // ---------------------------------------------------------------------------
  1354: // Settings tab
  1355: // ---------------------------------------------------------------------------
  1356: 
  1357: @Composable
  1358: private fun UserSettingsTabContent(
  1359:     currentSqrtMode: SqrtMode,
  1360:     onModeSelected: (SqrtMode) -&gt; Unit,
  1361:     variables: Map&lt;String, Double?&gt;,
  1362:     onVariableSet: (String, Double?) -&gt; Unit,
  1363:     vm: CalculatorViewModel
  1364: ) {
  1365:     val context = LocalContext.current
  1366:     Column(
  1367:         modifier = Modifier
  1368:             .fillMaxSize()
  1369:             .clip(RoundedCornerShape(24.dp))
  1370:             .background(AppGray)
  1371:             .border(1.dp, AppGrayBorder, RoundedCornerShape(24.dp))
  1372:             .verticalScroll(rememberScrollState())
  1373:             .padding(20.dp),
  1374:         verticalArrangement = Arrangement.spacedBy(16.dp)
  1375:     ) {
  1376:         // ── Sqrt section ───────────────────────────────────────────────────
  1377:         Text(&quot;Square root behavior&quot;, color = AppBlackSoft, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
  1378: 
  1379:         SettingsChoiceRow(
  1380:             title = &quot;Always add square root around item&quot;,
  1381:             subtitle = &quot;Wrap the current expression with √(…)&quot;,
  1382:             selected = currentSqrtMode == SqrtMode.WrapCurrentInput,
  1383:             onClick = { onModeSelected(SqrtMode.WrapCurrentInput) }
  1384:         )
  1385:         SettingsChoiceRow(
  1386:             title = &quot;Always add square root at the end&quot;,
  1387:             subtitle = &quot;Keep the current append behavior&quot;,
  1388:             selected = currentSqrtMode == SqrtMode.AppendToEnd,
  1389:             onClick = { onModeSelected(SqrtMode.AppendToEnd) }
  1390:         )
  1391:         SettingsChoiceRow(
  1392:             title = &quot;Ask every time&quot;,
  1393:             subtitle = &quot;Show a quick choice when you tap √&quot;,
  1394:             selected = currentSqrtMode == SqrtMode.AskEveryTime,
  1395:             onClick = { onModeSelected(SqrtMode.AskEveryTime) }
  1396:         )
  1397: 
  1398:         Spacer(Modifier.height(4.dp))
  1399: 
  1400:         // ── Variables section ──────────────────────────────────────────────
  1401:         Row(
  1402:             modifier = Modifier.fillMaxWidth(),
  1403:             verticalAlignment = Alignment.CenterVertically,
  1404:             horizontalArrangement = Arrangement.SpaceBetween
  1405:         ) {
  1406:             Text(&quot;Variables&quot;, color = AppBlackSoft, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
  1407:             if (vm.variables.size - 1 &lt; 10) { // exclude ans
  1408:                 TextButton(onClick = {
  1409:                     val nextNum = (1..10).firstOrNull { &quot;v$it&quot; !in vm.variables } ?: return@TextButton
  1410:                     vm.setVariable(&quot;v$nextNum&quot;, null)
  1411:                 }) {
  1412:                     Text(&quot;+ Add variable&quot;, color = AppGreen, fontWeight = FontWeight.Bold)
  1413:                 }
  1414:             }
  1415:         }
  1416: 
  1417:         Text(
  1418:             text = &quot;Tap a variable chip on the calculator to insert it into an expression. &quot; +
  1419:                     &quot;Use the = chip to write an assignment like \$v1 = 10.&quot;,
  1420:             color = AppGrayMuted,
  1421:             fontSize = 13.sp
  1422:         )
  1423: 
  1424:         vm.variables.keys.filter { it != ANS_VAR }.sortedBy { it.removePrefix(&quot;v&quot;).toIntOrNull() ?: 99 }.forEach { name -&gt;
  1425:             VariableEditorRow(
  1426:                 label = &quot;\$$name&quot;,
  1427:                 value = vm.variables[name],
  1428:                 onSave = { vm.setVariable(name, it) },
  1429:                 onDelete = if (vm.variables.size &gt; 2) { { vm.removeVariable(name) } } else null
  1430:             )
  1431:         }
  1432: 
  1433:         // $ans — read-only, auto-set by calculations
  1434:         val ansValue = vm.variables[ANS_VAR]
  1435:         Row(
  1436:             modifier = Modifier
  1437:                 .fillMaxWidth()
  1438:                 .clip(RoundedCornerShape(18.dp))
  1439:                 .background(AppGreenSoft)
  1440:                 .border(1.dp, AppGreen, RoundedCornerShape(18.dp))
  1441:                 .padding(horizontal = 16.dp, vertical = 12.dp),
  1442:             horizontalArrangement = Arrangement.spacedBy(12.dp),
  1443:             verticalAlignment = Alignment.CenterVertically
  1444:         ) {
  1445:             Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
  1446:                 Text(&quot;\$ans&quot;, color = AppGreenDeep, fontSize = 15.sp, fontWeight = FontWeight.Bold)
  1447:                 Text(
  1448:                     text = &quot;Auto-set by every calculation&quot;,
  1449:                     color = AppGreenDeep,
  1450:                     fontSize = 12.sp
  1451:                 )
  1452:             }
  1453:             Text(
  1454:                 text = if (ansValue != null) formatNumber(ansValue) else &quot;—&quot;,
  1455:                 color = AppGreenDeep,
  1456:                 fontSize = 16.sp,
  1457:                 fontWeight = FontWeight.Bold
  1458:             )
  1459:         }
  1460: 
  1461:         Spacer(Modifier.height(4.dp))
  1462: 
  1463:         // ── Gesture Typing Section ─────────────────────────────────────────
  1464:         Text(&quot;Gestures&quot;, color = AppBlackSoft, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
  1465:         SettingsChoiceRow(
  1466:             title = &quot;Enable slide-to-type&quot;,
  1467:             subtitle = &quot;Type equations by sliding through buttons&quot;,
  1468:             selected = vm.isGestureTypingEnabled,
  1469:             onClick = {
  1470:                 vm.isGestureTypingEnabled = !vm.isGestureTypingEnabled
  1471:                 saveGestureTypingEnabled(context, vm.isGestureTypingEnabled)
  1472:             }
  1473:         )
  1474: 
  1475:         Spacer(Modifier.height(4.dp))
  1476: 
  1477:         // ── Shortcuts section ──────────────────────────────────────────────
  1478:         Row(
  1479:             modifier = Modifier.fillMaxWidth(),
  1480:             verticalAlignment = Alignment.CenterVertically,
  1481:             horizontalArrangement = Arrangement.SpaceBetween
  1482:         ) {
  1483:             Text(&quot;Shortcuts&quot;, color = AppBlackSoft, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
  1484:             if (vm.shortcuts.size &lt; 10) {
  1485:                 TextButton(onClick = {
  1486:                     // Open a dialog or just add an empty one?
  1487:                     // Let&apos;s add an empty one with a unique name
  1488:                     val nextNum = (1..10).firstOrNull { &quot;S$it&quot; !in vm.shortcuts } ?: return@TextButton
  1489:                     vm.setShortcut(&quot;S$nextNum&quot;, &quot;&quot;)
  1490:                 }) {
  1491:                     Text(&quot;+ Add shortcut&quot;, color = AppGreen, fontWeight = FontWeight.Bold)
  1492:                 }
  1493:             }
  1494:         }
  1495: 
  1496:         Row(
  1497:             modifier = Modifier.fillMaxWidth(),
  1498:             verticalAlignment = Alignment.CenterVertically,
  1499:             horizontalArrangement = Arrangement.SpaceBetween
  1500:         ) {
  1501:             Text(&quot;Detection threshold&quot;, color = AppBlackSoft, fontSize = 14.sp)
  1502:             Row(verticalAlignment = Alignment.CenterVertically) {
  1503:                 IconButton(onClick = {
  1504:                     if (vm.shortcutThreshold &gt; 1) {
  1505:                         vm.shortcutThreshold--
  1506:                         saveShortcutThreshold(context, vm.shortcutThreshold)
  1507:                     }
  1508:                 }) { Text(&quot;-&quot;, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
  1509:                 
  1510:                 Text(vm.shortcutThreshold.toString(), fontWeight = FontWeight.Bold)
  1511:                 
  1512:                 IconButton(onClick = {
  1513:                     vm.shortcutThreshold++
  1514:                     saveShortcutThreshold(context, vm.shortcutThreshold)
  1515:                 }) { Text(&quot;+&quot;, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
  1516:             }
  1517:         }
  1518: 
  1519:         if (vm.shortcuts.isEmpty()) {
  1520:             Text(&quot;No shortcuts created yet.&quot;, color = AppGrayMuted, fontSize = 13.sp)
  1521:         }
  1522: 
  1523:         vm.shortcuts.forEach { (name, value) -&gt;
  1524:             ShortcutEditorRow(
  1525:                 name = name,
  1526:                 value = value,
  1527:                 onSave = { newName, newVal -&gt;
  1528:                     if (newName != name) vm.setShortcut(name, null)
  1529:                     vm.setShortcut(newName, newVal)
  1530:                 },
  1531:                 onDelete = { vm.setShortcut(name, null) }
  1532:             )
  1533:         }
  1534:     }
  1535: }
  1536: 
  1537: @Composable
  1538: private fun ShortcutEditorRow(
  1539:     name: String,
  1540:     value: String,
  1541:     onSave: (String, String) -&gt; Unit,
  1542:     onDelete: () -&gt; Unit
  1543: ) {
  1544:     var editName by remember(name) { mutableStateOf(name) }
  1545:     var editValue by remember(value) { mutableStateOf(value) }
  1546:     
  1547:     Column(
  1548:         modifier = Modifier
  1549:             .fillMaxWidth()
  1550:             .clip(RoundedCornerShape(18.dp))
  1551:             .background(AppWhite)
  1552:             .border(1.dp, AppGrayBorder, RoundedCornerShape(18.dp))
  1553:             .padding(16.dp),
  1554:         verticalArrangement = Arrangement.spacedBy(8.dp)
  1555:     ) {
  1556:         Row(
  1557:             modifier = Modifier.fillMaxWidth(),
  1558:             horizontalArrangement = Arrangement.spacedBy(12.dp),
  1559:             verticalAlignment = Alignment.CenterVertically
  1560:         ) {
  1561:             Text(&quot;\$&quot;, fontWeight = FontWeight.Bold)
  1562:             OutlinedTextField(
  1563:                 value = editName,
  1564:                 onValueChange = { editName = it },
  1565:                 modifier = Modifier.weight(1f),
  1566:                 label = { Text(&quot;Name&quot;) },
  1567:                 singleLine = true
  1568:             )
  1569:             IconButton(onClick = onDelete) {
  1570:                 Icon(Icons.Default.Delete, &quot;Delete&quot;, tint = Color.Red.copy(alpha = 0.6f))
  1571:             }
  1572:         }
  1573:         Row(
  1574:             modifier = Modifier.fillMaxWidth(),
  1575:             horizontalArrangement = Arrangement.spacedBy(12.dp),
  1576:             verticalAlignment = Alignment.CenterVertically
  1577:         ) {
  1578:             OutlinedTextField(
  1579:                 value = editValue,
  1580:                 onValueChange = { editValue = it },
  1581:                 modifier = Modifier.weight(1f),
  1582:                 label = { Text(&quot;Equation fragment&quot;) },
  1583:                 singleLine = true
  1584:             )
  1585:             TextButton(onClick = { onSave(editName, editValue) }) {
  1586:                 Text(&quot;Save&quot;, color = AppGreen, fontWeight = FontWeight.Bold)
  1587:             }
  1588:         }
  1589:     }
  1590: }
  1591: 
  1592: @Composable
  1593: private fun VariableEditorRow(
  1594:     label: String,
  1595:     value: Double?,
  1596:     onSave: (Double?) -&gt; Unit,
  1597:     onDelete: (() -&gt; Unit)? = null
  1598: ) {
  1599:     var text by remember(value) { mutableStateOf(value?.let { formatNumber(it) } ?: &quot;&quot;) }
  1600:     val keyboardController = LocalSoftwareKeyboardController.current
  1601: 
  1602:     val commit = {
  1603:         val parsed = text.trim().toDoubleOrNull()
  1604:         if (text.trim().isEmpty()) onSave(null) else if (parsed != null) onSave(parsed)
  1605:         keyboardController?.hide()
  1606:     }
  1607: 
  1608:     Row(
  1609:         modifier = Modifier
  1610:             .fillMaxWidth()
  1611:             .clip(RoundedCornerShape(18.dp))
  1612:             .background(AppWhite)
  1613:             .border(1.dp, AppGrayBorder, RoundedCornerShape(18.dp))
  1614:             .padding(horizontal = 16.dp, vertical = 8.dp),
  1615:         horizontalArrangement = Arrangement.spacedBy(12.dp),
  1616:         verticalAlignment = Alignment.CenterVertically
  1617:     ) {
  1618:         Text(
  1619:             text = label,
  1620:             color = AppBlack,
  1621:             fontSize = 15.sp,
  1622:             fontWeight = FontWeight.Bold,
  1623:             modifier = Modifier.width(52.dp)
  1624:         )
  1625: 
  1626:         OutlinedTextField(
  1627:             value = text,
  1628:             onValueChange = { text = it },
  1629:             modifier = Modifier.weight(1f),
  1630:             singleLine = true,
  1631:             placeholder = { Text(&quot;unset&quot;, color = AppGrayMuted, fontSize = 14.sp) },
  1632:             keyboardOptions = KeyboardOptions(
  1633:                 keyboardType = KeyboardType.Decimal,
  1634:                 imeAction = ImeAction.Done
  1635:             ),
  1636:             keyboardActions = KeyboardActions(onDone = { commit() }),
  1637:             colors = OutlinedTextFieldDefaults.colors(
  1638:                 focusedBorderColor = AppGreen,
  1639:                 unfocusedBorderColor = AppGrayBorder,
  1640:                 focusedTextColor = AppBlack,
  1641:                 unfocusedTextColor = AppBlack,
  1642:                 cursorColor = AppGreen
  1643:             ),
  1644:             textStyle = androidx.compose.ui.text.TextStyle(
  1645:                 fontSize = 14.sp,
  1646:                 fontWeight = FontWeight.Medium
  1647:             ),
  1648:             shape = RoundedCornerShape(12.dp)
  1649:         )
  1650: 
  1651:         TextButton(
  1652:             onClick = { commit() },
  1653:             modifier = Modifier.wrapContentWidth()
  1654:         ) {
  1655:             Text(&quot;Set&quot;, color = AppGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
  1656:         }
  1657:         
  1658:         if (onDelete != null) {
  1659:             IconButton(onClick = onDelete) {
  1660:                 Icon(Icons.Default.Delete, &quot;Delete&quot;, tint = Color.Red.copy(alpha = 0.6f))
  1661:             }
  1662:         }
  1663:     }
  1664: }
  1665: 
  1666: // ---------------------------------------------------------------------------
  1667: // Shared settings row
  1668: // ---------------------------------------------------------------------------
  1669: 
  1670: @Composable
  1671: private fun SettingsChoiceRow(
  1672:     title: String,
  1673:     subtitle: String,
  1674:     selected: Boolean,
  1675:     onClick: () -&gt; Unit
  1676: ) {
  1677:     Row(
  1678:         modifier = Modifier
  1679:             .fillMaxWidth()
  1680:             .clip(RoundedCornerShape(18.dp))
  1681:             .background(if (selected) AppGreenSoft else AppWhite)
  1682:             .border(1.dp, if (selected) AppGreen else AppGrayBorder, RoundedCornerShape(18.dp))
  1683:             .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }, onClick = onClick)
  1684:             .padding(16.dp),
  1685:         horizontalArrangement = Arrangement.spacedBy(12.dp),
  1686:         verticalAlignment = Alignment.CenterVertically
  1687:     ) {
  1688:         Box(
  1689:             modifier = Modifier
  1690:                 .height(18.dp)
  1691:                 .width(18.dp)
  1692:                 .clip(RoundedCornerShape(9.dp))
  1693:                 .background(if (selected) AppGreenDeep else AppGrayBorder)
  1694:         )
  1695:         Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
  1696:             Text(text = title, color = AppBlack, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
  1697:             Text(text = subtitle, color = AppBlackSoft, fontSize = 13.sp)
  1698:         }
  1699:     }
  1700: }
  1701: 
  1702: // ---------------------------------------------------------------------------
  1703: // Calculator buttons
  1704: // ---------------------------------------------------------------------------
  1705: 
  1706: @Composable
  1707: private fun ButtonRow(
  1708:     keys: List&lt;CalcKey&gt;,
  1709:     vm: CalculatorViewModel,
  1710:     operatorKey: CalcKey? = null,
  1711:     extraKey: CalcKey? = null,
  1712:     onSqrtClick: (() -&gt; Unit)? = null
  1713: ) {
  1714:     Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
  1715:         keys.forEach { key -&gt;
  1716:             CalculatorButton(
  1717:                 text = key.label,
  1718:                 style = key.style,
  1719:                 vm = vm,
  1720:                 modifier = Modifier.weight(key.widthWeight).height(60.dp),
  1721:                 onClick = { if (key.label == &quot;√&quot;) onSqrtClick?.invoke() else vm.onKey(key.label) }
  1722:             )
  1723:         }
  1724:         operatorKey?.let {
  1725:             CalculatorButton(
  1726:                 text = it.label, style = it.style,
  1727:                 vm = vm,
  1728:                 modifier = Modifier.weight(it.widthWeight).height(60.dp),
  1729:                 onClick = { vm.onKey(it.label) }
  1730:             )
  1731:         }
  1732:         extraKey?.let {
  1733:             CalculatorButton(
  1734:                 text = it.label, style = it.style,
  1735:                 vm = vm,
  1736:                 modifier = Modifier.weight(it.widthWeight).height(60.dp),
  1737:                 onClick = { vm.onKey(it.label) }
  1738:             )
  1739:         }
  1740:     }
  1741: }
  1742: 
  1743: @Composable
  1744: private fun CalculatorButton(
  1745:     text: String,
  1746:     style: KeyStyle,
  1747:     modifier: Modifier = Modifier,
  1748:     vm: CalculatorViewModel? = null,
  1749:     onClick: () -&gt; Unit
  1750: ) {
  1751:     val interactionSource = remember { MutableInteractionSource() }
  1752:     val pressed by interactionSource.collectIsPressedAsState()
  1753:     val scale by animateFloatAsState(
  1754:         targetValue = if (pressed) 0.97f else 1f,
  1755:         animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium),
  1756:         label = &quot;btnScale&quot;
  1757:     )
  1758:     val bg by animateColorAsState(
  1759:         targetValue = when (style) {
  1760:             KeyStyle.Equal -&gt; if (pressed) AppGreenSoft else AppGreen
  1761:             KeyStyle.Operation, KeyStyle.Utility -&gt; if (pressed) AppGray else AppGrayMuted
  1762:             KeyStyle.Digit, KeyStyle.Default -&gt; if (pressed) AppGray else AppWhite
  1763:         },
  1764:         animationSpec = tween(140),
  1765:         label = &quot;btnBg&quot;
  1766:     )
  1767:     val textColor by animateColorAsState(
  1768:         targetValue = when (style) {
  1769:             KeyStyle.Equal -&gt; AppWhite
  1770:             else -&gt; AppBlack
  1771:         },
  1772:         animationSpec = tween(140),
  1773:         label = &quot;btnText&quot;
  1774:     )
  1775:     val borderColor = when (style) {
  1776:         KeyStyle.Equal -&gt; AppGreenDeep
  1777:         KeyStyle.Operation -&gt; AppGray
  1778:         KeyStyle.Utility, KeyStyle.Digit, KeyStyle.Default -&gt; AppGrayBorder
  1779:     }
  1780: 
  1781:     Box(
  1782:         modifier = modifier
  1783:             .scale(scale)
  1784:             .clip(RoundedCornerShape(20.dp))
  1785:             .background(bg)
  1786:             .border(1.dp, borderColor, RoundedCornerShape(20.dp))
  1787:             .onGloballyPositioned { coords -&gt;
  1788:                 if (vm != null) {
  1789:                     vm.buttonBounds[text] = coords.positionInWindow()
  1790:                         .let { androidx.compose.ui.geometry.Rect(it, coords.size.toSize()) }
  1791:                 }
  1792:             }
  1793:             .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
  1794:         contentAlignment = Alignment.Center
  1795:     ) {
  1796:         Text(
  1797:             text = text,
  1798:             color = textColor,
  1799:             fontSize = when (style) {
  1800:                 KeyStyle.Utility -&gt; 18.sp
  1801:                 KeyStyle.Equal -&gt; 24.sp
  1802:                 else -&gt; 22.sp
  1803:             },
  1804:             fontWeight = FontWeight.Bold,
  1805:             textAlign = TextAlign.Center
  1806:         )
  1807:     }
  1808: }
  1809: 
  1810: @Preview(showBackground = true, widthDp = 360, heightDp = 800)
  1811: @Composable
  1812: private fun CalculatorPreview() {
  1813:     CalculatorTheme {
  1814:         CalculatorScreen()
  1815:     }
  1816: }
  1817: 

[Showing lines 1-1817 of 1817 total] [End of file]
</function_results>
<function_calls>
<invoke name="ViewFile">
<parameter name="path">app/src/main/java/com/example/calculator/CalculatorEngine.kt</parameter>
<parameter name="lineNumbers">True</parameter>
</invoke>
</function_calls>
<function_results>
1: package com.example.calculator
  2: 
  3: import kotlin.math.floor
  4: import kotlin.math.pow
  5: import kotlin.math.sqrt
  6: 
  7: object CalculatorEngine {
  8: 
  9:     fun formatNumber(v: Double): String =
  10:         if (v == floor(v) &amp;&amp; !v.isInfinite()) v.toLong().toString()
  11:         else &quot;%.10g&quot;.format(v).trimEnd(&apos;0&apos;).trimEnd(&apos;.&apos;)
  12: 
  13:     fun safeEvaluate(expr: String, variables: Map&lt;String, Double?&gt;, shortcuts: Map&lt;String, String&gt; = emptyMap()): String {
  14:         if (expr.isEmpty()) return &quot;0&quot;
  15:         return try {
  16:             val cleanedExpr = preProcessVoiceQuery(expr)
  17:             val expandedExpr = expandShortcuts(cleanedExpr, shortcuts)
  18:             val value = evaluate(expandedExpr, variables)
  19:             if (value.isNaN() || value.isInfinite()) &quot;Error&quot; else formatNumber(value)
  20:         } catch (_: Exception) {
  21:             &quot;Error&quot;
  22:         }
  23:     }
  24: 
  25:     private fun preProcessVoiceQuery(query: String): String {
  26:         return query.lowercase()
  27:             .replace(&quot;square root&quot;, &quot;√&quot;)
  28:             .replace(&quot;root&quot;, &quot;√&quot;)
  29:             .replace(&quot;parentheses&quot;, &quot;(&quot;)
  30:             .replace(&quot;open bracket&quot;, &quot;(&quot;)
  31:             .replace(&quot;closed bracket&quot;, &quot;)&quot;)
  32:             .replace(&quot;close bracket&quot;, &quot;)&quot;)
  33:             .replace(&quot;to the power of&quot;, &quot;^&quot;)
  34:             .replace(&quot;times&quot;, &quot;×&quot;)
  35:             .replace(&quot;multiplied by&quot;, &quot;×&quot;)
  36:             .replace(&quot;divided by&quot;, &quot;÷&quot;)
  37:             .replace(&quot;plus&quot;, &quot;+&quot;)
  38:             .replace(&quot;minus&quot;, &quot;−&quot;)
  39:     }
  40: 
  41:     private fun expandShortcuts(expr: String, shortcuts: Map&lt;String, String&gt;): String {
  42:         var currentExpr = expr
  43:         // Expand shortcuts iteratively in case of nested shortcuts (max 10 depth to prevent recursion)
  44:         for (i in 0 until 10) {
  45:             var expanded = false
  46:             for ((name, value) in shortcuts) {
  47:                 val token = &quot;\$$name&quot;
  48:                 if (currentExpr.contains(token)) {
  49:                     currentExpr = currentExpr.replace(token, value)
  50:                     expanded = true
  51:                 }
  52:             }
  53:             if (!expanded) break
  54:         }
  55:         return currentExpr
  56:     }
  57: 
  58:     private fun evaluate(expr: String, variables: Map&lt;String, Double?&gt;): Double {
  59:         val tokens = tokenize(expr)
  60:         val pos = intArrayOf(0)
  61:         val result = parseAddSub(tokens, pos, variables)
  62:         if (pos[0] != tokens.size) throw IllegalArgumentException(&quot;Unexpected token&quot;)
  63:         return result
  64:     }
  65: 
  66:     private fun tokenize(expr: String): List&lt;String&gt; {
  67:         val normalized = expr
  68:             .replace(&apos;×&apos;, &apos;*&apos;)
  69:             .replace(&apos;÷&apos;, &apos;/&apos;)
  70:             .replace(&apos;−&apos;, &apos;-&apos;)
  71:         val list = mutableListOf&lt;String&gt;()
  72:         var i = 0
  73:         while (i &lt; normalized.length) {
  74:             val c = normalized[i]
  75:             when {
  76:                 c == &apos;$&apos; -&gt; {
  77:                     val sb = StringBuilder()
  78:                     sb.append(&apos;$&apos;)
  79:                     i++
  80:                     while (i &lt; normalized.length &amp;&amp; (normalized[i].isLetter() || normalized[i].isDigit())) {
  81:                         sb.append(normalized[i++])
  82:                     }
  83:                     list += sb.toString()
  84:                 }
  85:                 c.isDigit() || c == &apos;.&apos; -&gt; {
  86:                     val sb = StringBuilder()
  87:                     while (i &lt; normalized.length &amp;&amp; (normalized[i].isDigit() || normalized[i] == &apos;.&apos;)) {
  88:                         sb.append(normalized[i++])
  89:                     }
  90:                     list += sb.toString()
  91:                 }
  92:                 normalized.startsWith(&quot;sqrt&quot;, i) -&gt; {
  93:                     list += &quot;√&quot;
  94:                     i += 4
  95:                 }
  96:                 c in listOf(&apos;+&apos;, &apos;-&apos;, &apos;*&apos;, &apos;/&apos;, &apos;^&apos;, &apos;(&apos;, &apos;)&apos;, &apos;%&apos;, &apos;√&apos;) -&gt; {
  97:                     list += c.toString()
  98:                     i++
  99:                 }
  100:                 c.isWhitespace() || c == &apos;=&apos; -&gt; i++
  101:                 else -&gt; i++
  102:             }
  103:         }
  104:         return list
  105:     }
  106: 
  107:     private fun parseAddSub(tokens: List&lt;String&gt;, pos: IntArray, variables: Map&lt;String, Double?&gt;): Double {
  108:         var value = parseMulDiv(tokens, pos, variables)
  109:         while (pos[0] &lt; tokens.size &amp;&amp; tokens[pos[0]] in listOf(&quot;+&quot;, &quot;-&quot;)) {
  110:             val op = tokens[pos[0]++]
  111:             val right = parseMulDiv(tokens, pos, variables)
  112:             value = if (op == &quot;+&quot;) value + right else value - right
  113:         }
  114:         return value
  115:     }
  116: 
  117:     private fun parseMulDiv(tokens: List&lt;String&gt;, pos: IntArray, variables: Map&lt;String, Double?&gt;): Double {
  118:         var value = parsePower(tokens, pos, variables)
  119:         while (pos[0] &lt; tokens.size) {
  120:             when {
  121:                 tokens[pos[0]] in listOf(&quot;*&quot;, &quot;/&quot;) -&gt; {
  122:                     val op = tokens[pos[0]++]
  123:                     val right = parsePower(tokens, pos, variables)
  124:                     value = if (op == &quot;*&quot;) value * right else value / right
  125:                 }
  126:                 tokens[pos[0]].startsImplicitMultiplication() -&gt; {
  127:                     val right = parsePower(tokens, pos, variables)
  128:                     value *= right
  129:                 }
  130:                 else -&gt; return value
  131:             }
  132:         }
  133:         return value
  134:     }
  135: 
  136:     private fun parsePower(tokens: List&lt;String&gt;, pos: IntArray, variables: Map&lt;String, Double?&gt;): Double {
  137:         var value = parseUnary(tokens, pos, variables)
  138:         if (pos[0] &lt; tokens.size &amp;&amp; tokens[pos[0]] == &quot;^&quot;) {
  139:             pos[0]++
  140:             val exponent = parsePower(tokens, pos, variables)
  141:             value = value.pow(exponent)
  142:         }
  143:         return value
  144:     }
  145: 
  146:     private fun parseUnary(tokens: List&lt;String&gt;, pos: IntArray, variables: Map&lt;String, Double?&gt;): Double {
  147:         if (pos[0] &gt;= tokens.size) throw IllegalArgumentException(&quot;Unexpected end&quot;)
  148:         return when (tokens[pos[0]]) {
  149:             &quot;+&quot; -&gt; { pos[0]++; parseUnary(tokens, pos, variables) }
  150:             &quot;-&quot; -&gt; { pos[0]++; -parseUnary(tokens, pos, variables) }
  151:             &quot;√&quot; -&gt; { pos[0]++; sqrt(parseUnary(tokens, pos, variables)) }
  152:             else -&gt; parsePrimary(tokens, pos, variables)
  153:         }
  154:     }
  155: 
  156:     private fun parsePrimary(tokens: List&lt;String&gt;, pos: IntArray, variables: Map&lt;String, Double?&gt;): Double {
  157:         if (pos[0] &gt;= tokens.size) throw IllegalArgumentException(&quot;Unexpected end&quot;)
  158:         val token = tokens[pos[0]++]
  159:         return when {
  160:             token == &quot;(&quot; -&gt; {
  161:                 val value = parseAddSub(tokens, pos, variables)
  162:                 if (pos[0] &gt;= tokens.size || tokens[pos[0]] != &quot;)&quot;) {
  163:                     throw IllegalArgumentException(&quot;Missing closing parenthesis&quot;)
  164:                 }
  165:                 pos[0]++
  166:                 applyPercentIfNeeded(tokens, pos, value)
  167:             }
  168:             token.startsWith(&quot;$&quot;) -&gt; {
  169:                 val varName = token.drop(1)
  170:                 val value = variables[varName]
  171:                     ?: throw IllegalArgumentException(&quot;Variable $token is not set&quot;)
  172:                 applyPercentIfNeeded(tokens, pos, value)
  173:             }
  174:             else -&gt; {
  175:                 val value = token.toDouble()
  176:                 applyPercentIfNeeded(tokens, pos, value)
  177:             }
  178:         }
  179:     }
  180: 
  181:     private fun applyPercentIfNeeded(tokens: List&lt;String&gt;, pos: IntArray, value: Double): Double {
  182:         var result = value
  183:         while (pos[0] &lt; tokens.size &amp;&amp; tokens[pos[0]] == &quot;%&quot;) {
  184:             pos[0]++
  185:             result /= 100.0
  186:         }
  187:         return result
  188:     }
  189: 
  190:     private fun String.startsImplicitMultiplication(): Boolean =
  191:         this == &quot;(&quot; || this == &quot;√&quot; || firstOrNull()?.isDigit() == true || this == &quot;.&quot; || startsWith(&quot;$&quot;)
  192: }
  193: 

[Showing lines 1-193 of 193 total] [End of file]
</function_results>
<function_calls>
<invoke name="ViewFile">
<parameter name="path">app/src/main/java/com/example/calculator/CalculatorState.kt</parameter>
<parameter name="lineNumbers">True</parameter>
</invoke>
</function_calls>
<function_results>
1: package com.example.calculator
  2: 
  3: import android.content.Context
  4: 
  5: object CalculatorState {
  6:     const val WIDGET_PREFS = &quot;widget_calculator_prefs&quot;
  7:     const val VARS_PREFS = &quot;user_vars_prefs&quot;
  8:     const val SHORTCUTS_PREFS = &quot;user_shortcuts_prefs&quot;
  9:     const val SETTINGS_PREFS = &quot;user_settings_prefs&quot;
  10: 
  11:     const val PREF_EXPRESSION = &quot;expression&quot;
  12:     const val PREF_DISPLAY = &quot;display&quot;
  13:     const val PREF_SHOULD_REPLACE = &quot;should_replace&quot;
  14:     
  15:     const val GESTURE_TYPING_KEY = &quot;gesture_typing_enabled&quot;
  16:     const val SHORTCUT_THRESHOLD_KEY = &quot;shortcut_threshold&quot;
  17: 
  18:     fun getExpression(context: Context): String =
  19:         context.getSharedPreferences(WIDGET_PREFS, Context.MODE_PRIVATE)
  20:             .getString(PREF_EXPRESSION, &quot;&quot;) ?: &quot;&quot;
  21: 
  22:     fun setWidgetState(context: Context, expression: String, display: String, shouldReplace: Boolean) {
  23:         context.getSharedPreferences(WIDGET_PREFS, Context.MODE_PRIVATE)
  24:             .edit()
  25:             .putString(PREF_EXPRESSION, expression)
  26:             .putString(PREF_DISPLAY, display)
  27:             .putBoolean(PREF_SHOULD_REPLACE, shouldReplace)
  28:             .apply()
  29:     }
  30:     
  31:     fun loadAllVariables(context: Context): MutableMap&lt;String, Double?&gt; {
  32:         val prefs = context.getSharedPreferences(VARS_PREFS, Context.MODE_PRIVATE)
  33:         val map = mutableMapOf&lt;String, Double?&gt;()
  34:         // Load whatever is there. Default starts with var1..var5 if empty? 
  35:         // No, let&apos;s just load all.
  36:         prefs.all.forEach { (key, value) -&gt;
  37:             if (key != &quot;ans&quot;) {
  38:                 val d = value.toString().toDoubleOrNull()
  39:                 map[key] = d
  40:             }
  41:         }
  42:         map[&quot;ans&quot;] = prefs.getString(&quot;ans&quot;, null)?.toDoubleOrNull()
  43:         return map
  44:     }
  45: 
  46:     fun loadShortcuts(context: Context): MutableMap&lt;String, String&gt; {
  47:         val prefs = context.getSharedPreferences(SHORTCUTS_PREFS, Context.MODE_PRIVATE)
  48:         val map = mutableMapOf&lt;String, String&gt;()
  49:         prefs.all.forEach { (key, value) -&gt;
  50:             if (value is String) map[key] = value
  51:         }
  52:         return map
  53:     }
  54: }
  55: 

[Showing lines 1-55 of 55 total] [End of file]
</function_results>
```
> keep going :)
