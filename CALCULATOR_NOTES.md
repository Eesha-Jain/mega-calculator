# Calculator App — Developer Notes
> **Purpose:** complete context for any AI/developer session — read this instead of re-reading source files.
> Keep this file updated whenever significant logic changes are made.

---

## Project layout

```
app/src/main/
  java/com/example/calculator/
    MainActivity.kt          ← entire app (single-file architecture)
  res/
    values/themes.xml        ← Theme.Calculator = Material3.Light.NoActionBar
    values/strings.xml
  AndroidManifest.xml
```

All logic — ViewModel, UI composables, evaluator — lives in `MainActivity.kt` (~1540 lines).

---

## Architecture

| Layer | Where |
|-------|-------|
| State + business logic | `CalculatorViewModel : ViewModel` |
| Root screen | `CalculatorScreen()` composable |
| Tab switcher | `CalculatorScreen` renders `CalculatorTabContent` or `SettingsTabContent` |
| Display + buttons | `CalculatorTabContent(vm)` |
| Evaluator | `evaluate(expr)` + `tokenize(expr)` (both inside the ViewModel) |

The ViewModel is obtained via `viewModel()` inside `CalculatorScreen` and passed down as a parameter.

---

## ViewModel: key state

```kotlin
var expression        by mutableStateOf("")          // the live expression string
var display           by mutableStateOf("0")         // what's shown in the big display
var shouldReplaceOnNextInput by mutableStateOf(false)// true right after = evaluation
var previousExpression by mutableStateOf<String?>(null) // grey recall text at top

// ── cursor / token-edit feature ──
var cursorPosition    by mutableStateOf<Int?>(null)  // char offset; null = append mode
val highlightedTokenIndex: Int?                      // COMPUTED (not stored state)
    // = index of display token whose END == cursorPosition
    // reads cursorPosition + tokenizeForDisplay() → auto-tracked by Compose
```

### Variables system
```kotlin
private val variables = mutableStateMapOf<String, Double?>()
// names: "ans", "var1" … "var10"
// display tokens: $ans, $var1 … $var10
private val ANS_VAR = "ans"
private val VAR_NAMES = listOf("var1","var2",…,"var10")
```

---

## Expression display: token model

`tokenizeForDisplay(expr: String = expression): List<String>`
- Groups consecutive digits/dots into one token ("123.4" → `["123.4"]`)
- Keeps `$varN` / `$ans` whole
- Every other char is its own single-char token
- Preserves original Unicode operators (×, ÷, −) verbatim — **no normalisation**

**Roundtrip guarantee:** `tokens.joinToString("") == expr` always holds.

---

## Cursor-mode editing (the "token edit" feature)

### How it's activated
`setCursor(tokenIndex: Int?)` — called when the user taps a token chip in the display.
- Computes `newPos = tokens.take(tokenIndex+1).sumOf { it.length }`  (end of that token)
- Toggles: if `cursorPosition == newPos` already → set to null (deselect)
- Tapping the display area (outside any token) calls `setCursor(null)` directly

### How keys work in cursor mode
`handleKeyAtCursor(key, cur)` in `onKey()` — intercepts when `cursorPosition != null`.

**Model: pure string insertion at character offset `cur`. Nothing smarter.**
```
expression = expression.substring(0, cur) + insert + expression.substring(cur)
cursorPosition = cur + insert.length
```

Key map (what gets inserted):
| key  | insert |
|------|--------|
| digit / "." | key itself |
| "÷" "×" "−" "+" | the Unicode char |
| "xʸ" | "^" |
| "√" "%" "(" ")" | verbatim |

Special keys:
- **⌫** — deletes `deleteLen` chars before cursor:
  - variable token (`$ans`, `$var1`…) → deletes whole token
  - " = " (3 chars) → deletes all 3
  - otherwise → deletes 1 char
- **C** — clears everything, exits cursor mode
- **=** — exits cursor mode then evaluates normally
- **+/-** — exits cursor mode then runs global sign toggle

### Example trace (the defining user example)
```
"3+5+4", tap token "5" → cursor = 3 (end of "5")
type (  → "3+5(+4"     cursor=4
type 5  → "3+5(5+4"    cursor=5
type +  → "3+5(5++4"   cursor=6   ← transient "++" is intentional / "nothing smarter"
type 2  → "3+5(5+2+4"  cursor=7
type +  → "3+5(5+2++4" cursor=8
type 4  → "3+5(5+2+4+4"cursor=9
type )  → "3+5(5+2+4)+4" ✓
```

---

## Append mode (default, cursor = null)

`onKey(key)` — the full `when(key)` block covers all calculator operations.

Notable behaviours:
- **shouldReplaceOnNextInput = true**: the next digit press clears the expression and starts fresh (set after `=` evaluation or repeat)
- **repeat `=`**: `performRepeat()` substitutes `$ans` for the leading number and re-evaluates
- **`+/-`**: wraps expression with unary minus; handled carefully for negative numbers
- **`√`**: controlled by `sqrtMode` setting (WrapCurrentInput vs AppendToEnd)
- **`(`**: auto-inserted before `(` if a number immediately precedes it
- **auto-zero before `.`**: `0` is prepended if expression is empty or ends with an operator
- **Assignment**: if expression matches `^\$(\w+)\s*=\s*(.+)$`, stores the variable

---

## Evaluator

`evaluate(expr: String): Double`
- `tokenize(expr)` normalises Unicode → ASCII operators, handles implicit multiplication
- Recursive descent: `parseAddSub → parseMulDiv → parsePow → parseUnary → parsePrimary`
- `parsePrimary` handles: numbers, variables, parentheses, √ prefix

`safeEvaluate(expr)` wraps evaluate and returns `"Error"` on exception/NaN/Inf.

---

## UI components

### `CalculatorTabContent(vm, showVarPanel)`
Main screen layout:
```
Column
  ├─ (optional) variable panel
  ├─ Display Box [weight 1f, clickable → setCursor(null)]
  │    ├─ previousExpression text (small grey, tap → loadPreviousExpression())
  │    ├─ Spacer [weight 1f]
  │    └─ IF expression.isNotEmpty() && !shouldReplaceOnNextInput:
  │         ExpressionTokenRow(tokens, highlightedTokenIndex, onTap=setCursor)
  │         + hint text if cursorPosition != null
  │       ELSE:
  │         AnimatedContent { Text(display, 36sp, bold) }
  └─ Button grid
```

### `ExpressionTokenRow(tokens, selectedIndex, onTokenTap)`
- Horizontally scrollable `Row`
- Auto-scrolls to end when `selectedIndex == null` (append mode)
- Each token: `Box(clip+RoundedCorner, green bg if selected, clickable)`
- Token font: **26sp bold**
- Selected visual: `AppGreenSoft` background + `AppGreen` 1dp border + `AppGreenDeep` text
- Toggle: tapping selected token calls `onTokenTap(same index)` → `setCursor` returns null

### Button layout
3-tab row (Calculator / History / Settings).
Buttons are defined as a flat list of `CalcButton` objects; the layout is a Column of Rows.
Each button press calls `vm.onKey(button.key)`.

### Colors (defined in `Color.kt`)
```kotlin
AppWhite      = #FFFFFF
AppBlack      = #1A1A1A
AppGreen      = #17A34A
AppGreenSoft  = #E7F8ED
AppGreenDeep  = #0F7A35
AppGrayMuted  = #9CA3AF
AppGrayBorder = #E5E7EB
```

---

## AndroidManifest notes
- `android:hardwareAccelerated="true"` — **must stay true**; disabling it causes Compose to render a black screen (Compose's compositing layers require GPU acceleration)
- `enableEdgeToEdge()` is called in `MainActivity.onCreate` — layout uses `systemBarsPadding()`
- No `window.setBackgroundDrawableResource` (was a band-aid; not needed)

---

## Known gaps / future work
1. **Range selection for wrapping**: selecting a range of tokens (e.g. `3×2`) and wrapping them in `()` — not yet implemented. Would require tracking a start + end cursor position.
2. **History tab**: the UI tab exists but the body is a placeholder.
3. **Variables panel**: accessible via the `Vars` chip; supports $var1–$var10 and $ans.
4. **Sqrt mode setting**: controlled by the Settings tab (`sqrtMode`: WrapCurrentInput | AppendToEnd).

---

## Recent changes log
| Session | Change |
|---------|--------|
| Initial | App created — evaluator, variable system, UI skeleton |
| Fix + feature | `hardwareAccelerated` → true (black-screen fix); `enableEdgeToEdge()` restored; token-display feature added (cursor-based string insertion model) |
