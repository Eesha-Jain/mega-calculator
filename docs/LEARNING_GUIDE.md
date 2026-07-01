# Android Development — Learning Guide
### *Using the Calculator App as Your Guide*

---

## Table of Contents

1. [What Is an Android App?](#1-what-is-an-android-app)
2. [The Project Structure](#2-the-project-structure)
3. [The Build System — Gradle](#3-the-build-system--gradle)
4. [The App's ID Card — AndroidManifest.xml](#4-the-apps-id-card--androidmanifestxml)
5. [The Programming Language — Kotlin](#5-the-programming-language--kotlin)
6. [The Entry Point — MainActivity & Activities](#6-the-entry-point--mainactivity--activities)
7. [Building UI — Jetpack Compose](#7-building-ui--jetpack-compose)
8. [Layout Composables — Column, Row, Box](#8-layout-composables--column-row-box)
9. [State — How the App Remembers Things](#9-state--how-the-app-remembers-things)
10. [The ViewModel — Separating Logic from UI](#10-the-viewmodel--separating-logic-from-ui)
11. [The Calculator Brain — Expression Evaluation](#11-the-calculator-brain--expression-evaluation)
12. [User Variables — State in the Evaluator](#12-user-variables--state-in-the-evaluator)
13. [Theming — Colors, Typography, and Themes](#13-theming--colors-typography-and-themes)
14. [Animation — Bringing the UI to Life](#14-animation--bringing-the-ui-to-life)
15. [Persisting Data — SharedPreferences](#15-persisting-data--sharedpreferences)
16. [Beyond the App — AppTileService](#16-beyond-the-app--apptileservice)
17. [The Big Picture — How It All Connects](#17-the-big-picture--how-it-all-connects)

---

## 1. What Is an Android App?

An Android app is a program that runs on Android devices. Under the hood it is a `.apk` file (Android Package) — a zip archive that contains:

- **Compiled code** (your Kotlin/Java logic, turned into bytecode)
- **Resources** (images, colors, text strings, icons)
- **A manifest** (a configuration file telling Android what's inside)

Android runs the app inside a **sandbox** — each app has its own isolated process and storage. Apps communicate with the operating system and with each other through a strict, permission-based system.

> **Key term — APK:** The installable file format for Android apps, like a `.exe` on Windows.

---

## 2. The Project Structure

Open the project root and you will see a very specific folder layout that Android Studio generates for every project. Here is what it means:

```
calculator/
├── app/                        ← Your actual app module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/calculator/   ← All Kotlin source code
│   │   │   ├── res/                           ← All resources (colors, strings, icons)
│   │   │   └── AndroidManifest.xml            ← App configuration
│   │   └── test/               ← Unit tests
│   └── build.gradle.kts        ← App-level build config
├── gradle/
│   └── libs.versions.toml      ← Centralized dependency versions
├── build.gradle.kts            ← Project-level build config
└── settings.gradle.kts         ← Declares which modules exist
```

### The `app/` Module

Almost every Android project has at least one **module**. A module is a self-contained unit of code that can be built independently. Your project has one module called `app` — this is the actual calculator application.

> **Key term — Module:** A discrete unit of code in a project. Large apps often split into many modules (e.g., `app`, `core`, `feature-settings`). This app keeps everything in one `app` module for simplicity.

### `src/main/java/` — Your Code

This is where all Kotlin files live. The path `com/example/calculator` mirrors the **package name** — a unique identifier for your app (like a reverse domain name). Every file in this project starts with:

```kotlin
package com.example.calculator
```

This tells the compiler which logical namespace the file belongs to.

### `src/main/res/` — Your Resources

Resources are non-code assets. This project has:

| Folder | Contains |
|---|---|
| `drawable/` | Vector icons (SVG-like XML files) |
| `mipmap-*/` | App launcher icons at various screen densities |
| `values/` | Color definitions, text strings, theme styles |
| `xml/` | Backup and data-extraction configuration |

> **Key term — Resources:** Everything that isn't code — images, colors, text, layouts. Android manages resources separately so they can be swapped easily (e.g., different languages, dark mode).

---

## 3. The Build System — Gradle

Before your code can run on a phone, it needs to be **compiled and packaged**. Gradle is the tool that does this. Think of it as a recipe book — it describes *how* to build your app.

### `libs.versions.toml` — The Version Catalog

```toml
[versions]
agp = "8.7.3"
kotlin = "2.1.0"
composeBom = "2024.12.01"

[libraries]
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
```

This file is a central list of every **library** and **plugin** the project uses, along with their version numbers. Instead of repeating version numbers in multiple files, you define them here once and reference them everywhere.

> **Key term — Dependency:** External code your app relies on. Instead of writing everything from scratch, you pull in pre-built libraries. For example, `material3` is Google's UI component library.

### `app/build.gradle.kts` — The App's Build Recipe

```kotlin
android {
    namespace = "com.example.calculator"
    compileSdk = 35        // Build against Android 15 APIs

    defaultConfig {
        applicationId = "com.example.calculator"  // Unique ID on the Play Store
        minSdk = 24        // Minimum Android version supported (Android 7.0)
        targetSdk = 35     // The Android version you've optimized for
        versionCode = 1    // Integer version (used internally)
        versionName = "1.0" // Human-readable version string
    }
}
```

- **`compileSdk`** — Which Android API version to compile against. Higher = access to newer APIs.
- **`minSdk`** — Devices running Android older than this cannot install the app.
- **`targetSdk`** — Tells Android which behaviors you've opted into.
- **`applicationId`** — Must be globally unique. This is the app's permanent identity on Google Play.

```kotlin
dependencies {
    implementation(libs.androidx.material3)
    implementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

- **`implementation`** — Include this library in the final app.
- **`testImplementation`** — Only for unit tests, not included in the release APK.
- **`androidTestImplementation`** — For tests that run on a real device or emulator.
- **`platform(bom)`** — A "Bill of Materials" that locks all Compose library versions together so they don't conflict.

> **Key term — BOM (Bill of Materials):** A special dependency that pins the versions of a whole family of libraries so they all stay compatible with each other.

---

## 4. The App's ID Card — AndroidManifest.xml

Every Android app must have an `AndroidManifest.xml`. It is the app's official declaration to the Android operating system — listing what the app contains and what it is allowed to do.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.Calculator">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".AppTileService"
            android:permission="android.permission.BIND_QUICK_SETTINGS_TILE"
            android:exported="true">
            <intent-filter>
                <action android:name="android.service.quicksettings.action.QS_TILE" />
            </intent-filter>
        </service>

    </application>
</manifest>
```

### Breaking It Down

| Element | What it does |
|---|---|
| `<application>` | Wraps all app components. Sets global icon, label, theme. |
| `android:icon` | The launcher icon. `@mipmap/ic_launcher` means "look in the mipmap resource folder." |
| `android:label` | The app's display name. `@string/app_name` pulls from `strings.xml`. |
| `<activity>` | Declares a screen. Android won't recognize screens you don't declare here. |
| `android:exported="true"` | This Activity/Service can be opened by other apps or the system launcher. |
| `<intent-filter>` | Describes what kinds of signals this component responds to. |
| `MAIN` + `LAUNCHER` | Together these mean: "this is the screen that opens when the user taps the app icon." |
| `<service>` | Declares a background service (the Quick Settings tile, covered in Section 15). |

> **Key term — Component:** The four building blocks of Android apps are Activities, Services, Broadcast Receivers, and Content Providers. Every one you use must be declared in the Manifest.

> **Key term — Intent:** A message object that requests an action. The Launcher sends an Intent to open `MainActivity`. The `<intent-filter>` tells Android which intents this component can handle.

### The `@` Notation

You'll see `@string/app_name`, `@mipmap/ic_launcher`, `@style/Theme.Calculator` everywhere. This is Android's **resource reference** syntax:

```
@ [resource type] / [resource name]
```

It links the manifest (or any XML file) to values defined elsewhere in `res/`.

---

## 5. The Programming Language — Kotlin

The app is written in **Kotlin** — a modern, concise language that runs on the Java Virtual Machine. Google has made Kotlin the official first-class language for Android development.

Here are the Kotlin features you'll encounter throughout this codebase:

### Variables: `val` vs `var`

```kotlin
val APP_NAME = "Calculator"   // Immutable — cannot be reassigned
var expression = ""           // Mutable — can change
```

- Use `val` by default. Only use `var` when something genuinely needs to change.

### Functions

```kotlin
private fun formatNumber(v: Double): String {
    return if (v == floor(v)) v.toLong().toString()
    else "%.10g".format(v)
}
```

- `fun` declares a function.
- `private` means only accessible within this file.
- `Double` is the parameter type, `String` is the return type.
- In Kotlin, `if` is an **expression** — it returns a value, so `return if (...)` works.

### Classes

```kotlin
class CalculatorViewModel : ViewModel() {
    var expression by mutableStateOf("")
        private set
    // ...
}
```

- `class CalculatorViewModel` defines a new class.
- `: ViewModel()` means it **inherits** from `ViewModel` — more on this in Section 10.
- `private set` means the property is publicly readable but only this class can change it.

### Enums

```kotlin
private enum class KeyStyle { Default, Digit, Utility, Operation, Equal }
private enum class SqrtMode { WrapCurrentInput, AppendToEnd, AskEveryTime }
private enum class MainTab { Calculator, UserSettings }
```

An **enum** is a fixed set of named constants. Here it's used to describe button visual styles, the behavior mode for the √ key, and which tab is active. Enums prevent typos — you can't accidentally write `KeyStyle.Operatoin`.

### Data Classes

```kotlin
private data class CalcKey(
    val label: String,
    val style: KeyStyle = KeyStyle.Default,
    val widthWeight: Float = 1f
)
```

A **data class** is a class whose main purpose is to hold data. Kotlin automatically generates `equals()`, `hashCode()`, `toString()`, and `copy()` for you. The `= KeyStyle.Default` syntax provides **default parameter values** — you don't have to pass `style` every time you create a `CalcKey`.

### Extension Functions

```kotlin
private fun String.startsImplicitMultiplication(): Boolean {
    return this == "(" || this == "√" || firstOrNull()?.isDigit() == true
}
```

An **extension function** adds a new method to an existing class without modifying it. Here, `.startsImplicitMultiplication()` is added to the built-in `String` class. Inside the function, `this` refers to the string it's called on:

```kotlin
tokens[pos[0]].startsImplicitMultiplication()  // called like a normal method
```

### `when` Expressions

```kotlin
when (key) {
    "C"  -> { expression = ""; display = "0" }
    "⌫"  -> { expression = expression.dropLast(1) }
    "="  -> { val result = safeEvaluate(expression) }
    else -> { expression += key }
}
```

`when` is Kotlin's supercharged `switch` statement. Each `->` is a branch. `else` is the default. It can also be used as an expression that returns a value.

### Null Safety

Kotlin eliminates a whole class of bugs by distinguishing between types that *can* be null and types that *cannot*:

```kotlin
val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
    ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // ?.  = "only call if not null"

if (launchIntent != null) {
    startActivityAndCollapse(launchIntent)
}
```

- `String` → cannot be null. The compiler forces you to always give it a value.
- `String?` → can be null. The compiler forces you to check before using it.
- `?.` is the **safe-call operator** — calls the method only if the object isn't null, otherwise returns null.

---

## 6. The Entry Point — MainActivity & Activities

### What is an Activity?

An **Activity** is a single screen in your app. It manages a window, handles user input, and controls what the user sees. The calculator has one Activity: `MainActivity`.

```kotlin
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
```

### The Activity Lifecycle

Android Activities have a well-defined **lifecycle** — a sequence of states managed by the operating system:

```
Created → Started → Resumed (user sees it & can interact)
                         ↓
                      Paused (another app partially covers it)
                         ↓
                      Stopped (not visible)
                         ↓
                    Destroyed (gone from memory)
```

Each state transition calls a **lifecycle callback** method. The most important is `onCreate()`, which fires once when the Activity is first created. This is where you set up your screen.

> **Key term — Lifecycle:** The sequence of states an Android component moves through. Understanding it prevents bugs like playing audio when the app is in the background.

### `override fun onCreate(...)`

`override` means "I'm replacing the parent class's version of this method." `super.onCreate(savedInstanceState)` calls the original version first — you must always do this in lifecycle methods.

**`savedInstanceState: Bundle?`** — A `Bundle` is a key-value container. If Android killed your app to free memory and then restores it, this bundle contains saved state data so you can restore the UI. The `?` means it can be null (on a fresh start, it is null).

### `enableEdgeToEdge()`

This single call makes the app draw behind the system status bar and navigation bar, giving a modern full-bleed look. The `systemBarsPadding()` modifier (used later in the UI) adds padding so content doesn't hide behind those bars.

### `setContent { ... }`

This is the magic bridge between the old Activity world and the new Compose world. It tells the Activity: "use this Composable function as your UI." From this point on, everything is Compose.

---

## 7. Building UI — Jetpack Compose

### What is Jetpack Compose?

**Jetpack Compose** is Android's modern UI toolkit. Before Compose, you built UIs by writing XML layout files and then finding each view in code with `findViewById()`. Compose replaces all of that with a **declarative** approach: you describe *what* the UI should look like given the current state, and Compose figures out *how* to draw it.

> **Key term — Declarative UI:** You describe the desired end state ("show a green button with the label '+'"). The framework handles the rendering. This contrasts with *imperative UI* where you manually manipulate individual view properties.

### `@Composable` Functions

The building block of every Compose UI is a function annotated with `@Composable`:

```kotlin
@Composable
fun CalculatorScreen(vm: CalculatorViewModel = viewModel()) {
    // ... UI description here
}
```

- The `@Composable` annotation tells the Compose compiler this function can describe UI.
- Composable functions can call other Composable functions — this is how you build complex UIs from small pieces.
- They are **not** regular functions. They don't return a value; instead, they *emit* UI elements.
- They can be called again at any time (this is called **recomposition**) — whenever the state they read changes, Compose re-runs the function to update the UI.

> **Key term — Recomposition:** When state changes, Compose automatically re-runs any @Composable functions that read that state, efficiently updating only what changed on screen.

### Modifiers

Almost every Composable accepts a `Modifier` parameter. A `Modifier` is a chain of instructions that describes how to lay out, draw, and interact with a composable:

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()              // expand to fill all available space
        .background(AppWhite)       // white background
        .systemBarsPadding()        // don't draw under status/nav bar
        .padding(horizontal = 20.dp, vertical = 18.dp)  // inner spacing
)
```

Modifiers are chained with `.` — each one wraps the previous. Order matters: `.padding().background()` is different from `.background().padding()`.

> **Key term — dp:** Density-independent pixels. Instead of real pixels (which vary wildly between screen densities), `dp` is a virtual unit that stays the same physical size across devices. `1dp = 1 pixel on a 160dpi screen`.

> **Key term — sp:** Scale-independent pixels. Like `dp` but also respects the user's font size preference. Always use `sp` for text sizes.

---

## 8. Layout Composables — Column, Row, Box

Compose has three fundamental layout building blocks. This app uses all three extensively.

### `Column` — Vertical Stack

Arranges children vertically from top to bottom:

```kotlin
Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(16.dp)  // 16dp gap between each child
) {
    Text("First")   // ↑ top
    Text("Second")  // ↓ below first
    Text("Third")   // ↓ below second
}
```

### `Row` — Horizontal Stack

Arranges children horizontally left to right:

```kotlin
Row(
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    modifier = Modifier.fillMaxWidth()
) {
    CalculatorButton(text = "7", ...)  // ← left
    CalculatorButton(text = "8", ...)  // → right of 7
    CalculatorButton(text = "9", ...)  // → right of 8
    CalculatorButton(text = "÷", ...)  // → right of 9
}
```

### `Box` — Layered Stack

Layers children on top of each other (like CSS `position: absolute`):

```kotlin
Box(
    modifier = Modifier.fillMaxWidth().height(200.dp),
    contentAlignment = Alignment.BottomEnd  // align children to bottom-right
) {
    Text("expression in small text")  // bottom layer
    Text("56pt result display")       // drawn on top
}
```

The display area of the calculator is a `Box` — it shows both the current expression and the large result number, aligned to the bottom-right.

### `weight()` — Flexible Sizing

Inside a `Row` or `Column`, `Modifier.weight(n)` distributes remaining space proportionally:

```kotlin
Row {
    CalculatorButton(modifier = Modifier.weight(2f), text = "0")  // takes 2/3 of width
    CalculatorButton(modifier = Modifier.weight(1f), text = ".")  // takes 1/3 of width
}
```

The "0" button is twice as wide as the "." button — which is why 0 spans two columns on the bottom row.

---

## 9. State — How the App Remembers Things

This is the most important concept in Compose.

### The Problem

In a plain Kotlin function, variables are reset every time the function runs:

```kotlin
fun counter() {
    var count = 0     // RESET every time — this never works
    count++
}
```

But Compose re-runs composable functions constantly (on every recomposition). If variables reset every time, nothing would persist.

### `remember` and `mutableStateOf`

Compose solves this with two tools used together:

```kotlin
var screenVisible by remember { mutableStateOf(false) }
var selectedTab   by remember { mutableStateOf(MainTab.Calculator) }
```

- **`mutableStateOf(value)`** creates a **state holder** — a special object that holds a value and, crucially, notifies Compose whenever that value changes.
- **`remember { ... }`** tells Compose to *remember* the result across recompositions. Without `remember`, `mutableStateOf` would create a brand new object on every recomposition.
- **`by`** is Kotlin's **property delegation** syntax — it lets you read and write `screenVisible` directly (instead of `screenVisible.value`).

> **Key term — State:** Data that, when it changes, should cause the UI to update. In Compose, state is held in `mutableStateOf` objects.

### How It Flows — Unidirectional Data Flow

This app follows a pattern called **unidirectional data flow (UDF)**:

```
User taps "7"
      ↓
onClick calls vm.onKey("7")
      ↓
ViewModel updates expression & display state
      ↓
Compose detects state change → recomposes affected UI
      ↓
Display shows "7"
```

State flows *down* (from ViewModel to UI). Events flow *up* (from UI to ViewModel). This one-way flow makes the app predictable and easy to debug.

### `LaunchedEffect`

```kotlin
LaunchedEffect(Unit) {
    screenVisible = true
}
```

`LaunchedEffect` runs a block of code as a side effect when a composable first appears. `Unit` means "run this once on first composition." Here it's used to trigger the fade-in animation the moment `CalculatorScreen` appears.

> **Key term — Side Effect:** Any work that reaches outside of the Compose UI system — like launching a coroutine, starting an animation, or calling an analytics SDK. Compose has special containers (`LaunchedEffect`, `SideEffect`, etc.) to control when side effects run.

---

## 10. The ViewModel — Separating Logic from UI

### Why Do We Need It?

Imagine the user rotates their phone. Android **destroys and recreates** the Activity. If your calculator's expression was stored directly in the Activity, it would be wiped out. The **ViewModel** survives configuration changes (like rotation).

```
Phone rotates → Activity destroyed → Activity recreated
                       ↕ ViewModel survives ↕
```

Additionally, the ViewModel keeps business logic (the calculator math) completely separate from the UI code (buttons and text). This separation makes code much easier to test and maintain.

### The CalculatorViewModel

```kotlin
class CalculatorViewModel : ViewModel() {

    var expression by mutableStateOf("")
        private set

    var display by mutableStateOf("0")
        private set

    private var shouldReplaceOnNextNumericInput = false

    fun onKey(key: String) { ... }
    fun applySqrtWrap() { ... }
    fun appendSqrtToEnd() { ... }
    private fun safeEvaluate(expr: String): String { ... }
}
```

- **`expression`** — The raw string the user is building (e.g., `"123+456"`).
- **`display`** — What's shown in the large display area. Usually matches `expression`, but after `=` is pressed it shows the formatted result.
- **`shouldReplaceOnNextNumericInput`** — A private flag: after a result is shown, the next digit typed should *replace* it (like a real calculator), not append to it.
- **`onKey(key: String)`** — The single entry point for all button taps. The entire calculator's input logic lives here.

### Using the ViewModel in Compose

```kotlin
@Composable
fun CalculatorScreen(vm: CalculatorViewModel = viewModel()) {
```

`viewModel()` is a special Compose function that retrieves the ViewModel. It creates it if it doesn't exist, or returns the existing one if the Activity was recreated. The `= viewModel()` is a **default argument** — you can pass a different ViewModel (useful for testing), or let it find one automatically.

---

## 11. The Calculator Brain — Expression Evaluation

This is the pure logic heart of the app, with no UI involvement at all.

### The Flow

```
User types: "√(9)+3×2"
                ↓
         onKey("=") called
                ↓
    trimTrailingOperator()    removes any dangling +, -, etc.
                ↓
        safeEvaluate()        wraps evaluate() in try/catch
                ↓
           evaluate()         calls tokenize(), then the parser
                ↓
          tokenize()          splits "√(9)+3×2" → ["√","(","9",")","+"," 3","×","2"]
                ↓
   parseAddSub / parseMulDiv  recursive descent parser
                ↓
          formatNumber()      3.0 → "3",  3.14159... → "3.14159..."
                ↓
   display = "9"   (√9 + 3×2 = 3 + 6 = 9)
```

### Tokenization

The **tokenizer** converts a raw string into a list of meaningful **tokens**:

```kotlin
private fun tokenize(expr: String): List<String> {
    val normalized = expr
        .replace('×', '*')
        .replace('÷', '/')
        .replace('−', '-')
    // ... then walk character by character, grouping digits, recognizing operators
}
```

First it normalizes the display symbols (`×`, `÷`) to their ASCII equivalents (`*`, `/`) since the parser only needs to handle one form. Then it walks through the string character by character, grouping multi-digit numbers and recognizing single-character operators.

### Recursive Descent Parsing

The parser evaluates expressions respecting **operator precedence** — multiplication before addition — using a technique called **recursive descent parsing**. Each function handles a specific level of precedence:

```
parseAddSub       (+, -)         ← lowest precedence
  └── parseMulDiv (×, /)
        └── parsePower  (^)
              └── parseUnary  (-, √)
                    └── parsePrimary  (numbers, parentheses)   ← highest precedence
```

Each level calls the next level for its operands, naturally making higher-precedence operations happen first. For example, `3 + 2 × 4`:

1. `parseAddSub` calls `parseMulDiv` for the left side → gets `3`
2. Sees `+`, calls `parseMulDiv` for the right side
3. `parseMulDiv` sees `2`, then `×`, then calls down to get `4` → returns `8`
4. `parseAddSub` returns `3 + 8 = 11` ✓

> **Key term — Recursive Descent Parser:** A parser where each grammar rule (addition, multiplication, etc.) is implemented as a function that calls other functions for sub-rules. It's elegant, readable, and handles precedence and parentheses naturally.

### `safeEvaluate` and Error Handling

```kotlin
private fun safeEvaluate(expr: String): String {
    if (expr.isEmpty()) return "0"
    return try {
        val value = evaluate(expr)
        if (value.isNaN() || value.isInfinite()) "Error"
        else formatNumber(value)
    } catch (_: Exception) {
        "Error"
    }
}
```

`try { ... } catch { ... }` is Kotlin's error handling. If `evaluate()` throws an exception (e.g., malformed input), execution jumps to `catch` and returns `"Error"` instead of crashing the app. `isNaN()` catches cases like `0÷0` (Not a Number) and `isInfinite()` catches `1÷0`.

---

## 12. User Variables — State in the Evaluator

So far, every calculation the app performs is stateless: you type an expression, press `=`, get a result, and the context is thrown away. User variables change this. They give the calculator a **memory** — named slots the user can store values in, reference in later expressions, and update over time.

The app supports ten user-defined variables (`$var1` through `$var10`) and one automatic variable (`$ans`) that is always set to the result of the most recent calculation. Variables can appear anywhere in an expression:

```
$var1 = 10            → stores 10 in $var1
$var1 + 5             → evaluates to 15  (and stores 15 in $ans)
$var2 = $var1 × 3     → stores 30 in $var2
($var1 + $var2) × 2   → evaluates to 80
```

This single feature touches every layer of the app at once: the **ViewModel** holds the variable map as observable state, the **tokenizer and parser** recognize and resolve variable tokens inside expressions, and **SharedPreferences** persists the values across sessions. It is a good capstone to study because it shows how all the earlier concepts compose together.

### `AndroidViewModel` — When the ViewModel Needs the Application

The original `CalculatorViewModel` extended plain `ViewModel`. Variables require reading and writing SharedPreferences, which needs a `Context`. ViewModels must never hold a reference to an Activity's `Context` directly — the Activity can be destroyed and recreated (e.g. on rotation) while the ViewModel lives on, and clinging to that stale reference causes a **memory leak**.

The safe alternative is the `Application` context, which lives for the entire lifetime of the app process. `AndroidViewModel` is a `ViewModel` subclass that receives and safely holds the `Application`:

```kotlin
class CalculatorViewModel(app: Application) : AndroidViewModel(app) {
    private val ctx get() = getApplication<Application>()
    // ...
}
```

- **`AndroidViewModel(app)`** — The constructor accepts the `Application` instance. Compose's `viewModel()` function handles passing it automatically because the host `ComponentActivity` provides the `Application` through the default `ViewModelProvider` factory.
- **`getApplication<Application>()`** — Retrieves the stored reference. The generic type just casts it if you have a custom `Application` subclass; the base `Application` type is sufficient here.
- The **`init { }`** block loads all eleven variables from SharedPreferences the moment the ViewModel is first created, before any UI renders.

> **Key term — Application:** The global singleton for your app's process. Unlike an `Activity`, it is created once when the app starts and only destroyed when the OS kills the whole process. Holding a reference to it from a ViewModel is always safe.

> **Why not just pass `context` into `onKey()`?** Passing context through UI event callbacks would tightly couple the UI layer to storage concerns — exactly the separation the ViewModel pattern is designed to prevent. Holding the `Application` inside the ViewModel keeps all storage logic self-contained.

### `mutableStateMapOf` — Reactive State for a Key-Value Store

`mutableStateOf` holds one value. For a dictionary of eleven variables, the app uses its map equivalent:

```kotlin
val variables = mutableStateMapOf<String, Double?>()
```

- **`mutableStateMapOf`** creates a Compose-aware `MutableMap`. Like `mutableStateOf`, it notifies every composable that reads from it whenever an entry is added, updated, or removed — triggering recomposition automatically.
- The key is the internal variable name (`"var1"` through `"var10"`, `"ans"`). The value is a `Double?` — nullable so that `null` means *not yet set*, shown as `—` on the chip.
- When a calculation updates `$ans`, the `$ans` chip on screen refreshes immediately with the new value — no extra notification code needed, because reading `variables["ans"]` inside the composable is enough to subscribe to changes.

```kotlin
init {
    variables.putAll(loadAllVariables(ctx))  // restore from disk on startup
}

fun setVariable(name: String, value: Double?) {
    variables[name] = value           // updates the map → triggers recomposition
    persistVariable(ctx, name, value) // writes to SharedPreferences → survives restarts
}
```

> **Key term — `mutableStateMapOf`:** A Compose-observable `MutableMap`. Any `@Composable` that reads from it recomposes automatically when any entry changes. Think of it as `mutableStateOf` applied to an entire dictionary at once.

### Variable Tokens in the Tokenizer

Before the parser can evaluate `$var1 + 5`, the tokenizer must recognize `$var1` as a single, meaningful token — not a stray `$` character followed by the text `var1`. A new case was added to the character-by-character scanner:

```kotlin
c == '$' -> {
    val sb = StringBuilder()
    sb.append('$')
    i++  // consume the '$' itself
    while (i < normalized.length && (normalized[i].isLetter() || normalized[i].isDigit())) {
        sb.append(normalized[i++])
    }
    list += sb.toString()  // produces e.g. "$var1", "$var10", "$ans"
}
```

When the scanner encounters a `$`, it keeps consuming letters and digits until the name ends, yielding a single token like `"$var1"`. This is exactly how the existing number-scanning case works — just with a `$` prefix triggering it and letters being valid body characters. The tokenizer hands these opaque string tokens off to the parser; the parser is responsible for knowing what to do with them.

The normalization step that replaces `×` with `*` and `÷` with `/` leaves `$` untouched, so variable tokens pass through intact.

### Variable Resolution in the Parser

Inside `parsePrimary` — the deepest level of the recursive-descent parser, which handles individual atomic values — variable tokens are resolved to their stored numbers:

```kotlin
token.startsWith("$") -> {
    val varName = token.drop(1)  // strip the "$", e.g. "var1"
    val value = variables[varName]
        ?: throw IllegalArgumentException("Variable $token is not set")
    applyPercentIfNeeded(tokens, pos, value)
}
```

- `token.drop(1)` removes the `$` prefix to get the map key.
- `variables[varName]` looks the current value up from the ViewModel's state map.
- The `?: throw` (**Elvis operator with throw**) means: "if the result is `null` (variable not set), throw an exception immediately." `safeEvaluate` wraps the whole evaluation in a `try/catch`, so the exception surfaces as `"Error"` on the display rather than a crash.
- `applyPercentIfNeeded` lets `$var1%` work just like `10%`, dividing the resolved value by 100.

Because the variable is resolved right down at the `parsePrimary` level, it participates naturally in the full expression — it can be negated (`-$var1`), wrapped in parentheses (`($var1 + $var2) × 3`), raised to a power (`$var1 ^ 2`), and passed through every layer of the recursive parser exactly like a plain number.

### Assignment Detection

A plain `=` button press evaluates the expression and stores the result in `$ans`. But when the expression matches the pattern `$varName = <expression>`, the calculator performs a **variable assignment** instead of a normal evaluation:

```kotlin
"=" -> {
    val trimmed = expression.trim()
    val assignMatch = Regex("""^\$(\w+)\s*=\s*(.+)$""").matchEntire(trimmed)

    if (assignMatch != null) {
        val varName = assignMatch.groupValues[1]  // e.g. "var1"
        val rhs     = assignMatch.groupValues[2].trim()  // the right-hand side
        if (isValidVarName(varName)) {
            val result = safeEvaluate(trimTrailingOperator(rhs))
            if (result != "Error") {
                setVariable(varName, result.toDoubleOrNull())
                display = "\$$varName = $result"
            } else { display = "Error" }
            expression = ""; return  // consumed — skip normal evaluation
        }
    }

    // No assignment match → normal evaluation, result stored in $ans
    val result = safeEvaluate(trimTrailingOperator(expression))
    if (result != "Error") setVariable(ANS_VAR, result.toDoubleOrNull())
    // ...
}
```

The regex `^\$(\w+)\s*=\s*(.+)$` does the following:
- `^\$` — the expression must start with a literal `$`
- `(\w+)` — captures the variable name (letters, digits, underscores)
- `\s*=\s*` — an `=` sign with optional whitespace on either side, so `$var1=10`, `$var1 = 10`, and `$var1  =  10` all match
- `(.+)$` — captures everything after the `=` as the right-hand side to evaluate

`isValidVarName` checks that the captured name is one of the eleven known names (`var1`–`var10`, `ans`), so a typo like `$var11 = 10` is rejected as an error rather than silently creating a rogue variable.

To write an assignment, the user taps a **variable chip** (which inserts e.g. `$var1` into the expression string), then taps the green **`=` chip** in the chip row (which calls `insertAssignEquals()` to append ` = ` — a *string insertion*, distinct from the evaluate `=` button), enters the right-hand side, and finally presses the evaluate `=` button to commit.

### Smarter Backspace

Variable tokens like `$var10` are six characters. Character-by-character deletion would force the user to press `⌫` six times to erase one token, leaving broken partial tokens like `$var1` or `$var` in the expression along the way. The backspace handler was made token-aware:

```kotlin
"⌫" -> {
    val endingToken = knownVarTokens().firstOrNull { expression.endsWith(it) }
    expression = when {
        endingToken != null        -> expression.dropLast(endingToken.length)
        expression.endsWith(" = ") -> expression.dropLast(3)  // delete the whole " = " unit
        else                       -> expression.dropLast(1)
    }
}
```

`knownVarTokens()` returns all eleven tokens sorted longest-first (`$var10` before `$var1`, etc.). Checking longest tokens first prevents a shorter token from matching as a suffix of a longer one — for example, without length-ordering, `$var1` might incorrectly match at the tail of `$var10`. In practice `"$var10".endsWith("$var1")` is already false (the last five characters are `"var10"`, not `"$var1"`), but sorting by length is good defensive practice. The ` = ` assignment operator is also deleted as a unit for the same reason.

### Variable Persistence

Variables are stored as strings in their own dedicated SharedPreferences file, separate from the sqrt-mode preference file:

```kotlin
private fun persistVariable(context: Context, name: String, value: Double?) {
    val editor = context.getSharedPreferences(VARS_PREFS, Context.MODE_PRIVATE).edit()
    if (value == null) editor.remove(name)
    else editor.putString(name, value.toBigDecimal().toPlainString())
    editor.apply()
}
```

Strings are used instead of `putFloat()` because `Float` (32-bit) has much less precision than `Double` (64-bit). A value like `123456789.123` would silently lose digits when round-tripped through a `Float`. Storing via `toBigDecimal().toPlainString()` — which produces a plain decimal string like `"123456789.123"` without scientific notation — and reading back with `toDoubleOrNull()` preserves the full `Double` precision.

When a user clears a variable (leaves the field blank and taps Set), `editor.remove(name)` deletes the key from the file entirely. On the next app launch, a missing key loads as `null` — the chip shows `—` and the evaluator throws an error if that variable is used in an expression before being given a value.

### The Variable Chip Row in the UI

All of the above logic is surfaced through a horizontally scrollable chip row that sits between the display and the button pad:

- Chips `$1` through `$10` map to `$var1`–`$var10`. Tapping one calls `vm.insertVariable("var1")` etc., which appends the token to the expression (or replaces a stale result, matching how digit keys behave after pressing `=`).
- The `$ans` chip (green-tinted) inserts `$ans`. Its value updates live on the chip as soon as any calculation completes, because the chip reads directly from `vm.variables["ans"]` — a `mutableStateMapOf` entry — and recomposes automatically.
- The green `=` chip inserts the ` = ` assignment operator string, enabling the user to build an assignment expression without a dedicated keyboard key.

In Settings, a scrollable variable editor lists all ten variables with editable `OutlinedTextField` fields and a **Set** button. `$ans` is shown read-only with a green background to make clear it is managed by the calculator, not the user.

---

## 13. Theming — Colors, Typography, and Themes

Good apps have a consistent visual language. Compose provides a **theming system** to define and share colors and typography across the whole app.

### Colors — `Color.kt`

```kotlin
val AppWhite    = Color(0xFFFFFFFF)
val AppBlack    = Color(0xFF111111)
val AppGreen    = Color(0xFF17A34A)
val AppGreenSoft = Color(0xFFE7F8ED)
```

Colors are defined as named constants. `Color(0xFFRRGGBB)` is a hex color — `FF` is full opacity (alpha), then two hex digits each for Red, Green, Blue.

By centralizing colors here, changing the app's green accent is a one-line change instead of hunting through 50 files.

### Typography — `Type.kt`

```kotlin
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 56.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    )
)
```

`Typography` defines a set of named text styles for the app. `displayLarge` is for big, prominent text (like the calculator result). `bodyLarge` is for normal body content.

### Theme — `Theme.kt`

```kotlin
private val AppColorScheme = lightColorScheme(
    primary = AppGreen,
    background = AppWhite,
    surface = AppGray,
    // ...
)

@Composable
fun CalculatorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content,
    )
}
```

`CalculatorTheme` is a wrapper composable that installs your colors and typography into Compose's **Material Theme** system. Any composable inside `CalculatorTheme { ... }` can access these values via `MaterialTheme.colorScheme.primary`, etc.

It's applied at the very top, in `MainActivity`:

```kotlin
setContent {
    CalculatorTheme {        // ← Theme wraps everything
        CalculatorScreen()
    }
}
```

> **Key term — Material Design:** Google's design system — a set of guidelines and components for building consistent, polished UIs. Material3 (also called Material You) is the latest version, used here.

---

## 14. Animation — Bringing the UI to Life

Compose makes animation straightforward with **animate*AsState** functions. These smoothly transition a value from its current state to a new target value.

### Screen Fade-in

```kotlin
var screenVisible by remember { mutableStateOf(false) }

LaunchedEffect(Unit) { screenVisible = true }  // flip to true on first frame

val screenAlpha by animateFloatAsState(
    targetValue = if (screenVisible) 1f else 0f,  // 0.0 = invisible, 1.0 = fully visible
    animationSpec = tween(300),                    // linear 300ms transition
    label = "screenAlpha"
)

// Applied to the root Column:
modifier = Modifier.alpha(screenAlpha)
```

When `screenVisible` flips from `false` to `true`, `animateFloatAsState` smoothly animates `screenAlpha` from `0f` to `1f` over 300ms, creating a fade-in effect.

### Button Press Animation

```kotlin
val interactionSource = remember { MutableInteractionSource() }
val pressed by interactionSource.collectIsPressedAsState()

val scale by animateFloatAsState(
    targetValue = if (pressed) 0.97f else 1f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMedium
    ),
    label = "btnScale"
)
```

- **`MutableInteractionSource`** tracks user interactions (press, focus, hover) on a composable.
- **`collectIsPressedAsState()`** converts the press state into a boolean state value.
- When pressed, the button scales down to 97% with a **spring animation** (which physically simulates a spring — it overshoots slightly and bounces back for a satisfying tactile feel).

### Display Number Animation

```kotlin
AnimatedContent(
    targetState = vm.display,
    transitionSpec = {
        fadeIn(animationSpec = tween(120)) togetherWith fadeOut(animationSpec = tween(90))
    }
) { displayValue ->
    Text(text = displayValue, fontSize = 56.sp, ...)
}
```

`AnimatedContent` animates whenever its `targetState` changes. When the display value changes (e.g., "12" → "123"), the old number fades out and the new one fades in, rather than just snapping. `togetherWith` makes the fade-in and fade-out run simultaneously.

---

## 15. Persisting Data — SharedPreferences

Variables and ViewModel state are stored in **memory** — they're lost when the app is killed. For settings that should persist between sessions, Android provides **SharedPreferences** — a simple key-value store saved to the device's internal storage.

### Saving the √ Mode Setting

```kotlin
private const val SETTINGS_PREFS = "user_settings_prefs"
private const val SQRT_MODE_KEY = "sqrt_mode"

private fun saveSqrtMode(context: Context, mode: SqrtMode) {
    context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        .edit()
        .putString(SQRT_MODE_KEY, mode.name)
        .apply()
}
```

- `getSharedPreferences("name", MODE_PRIVATE)` opens (or creates) a preference file named `"user_settings_prefs"`. `MODE_PRIVATE` means only this app can read it.
- `.edit()` opens the file for writing.
- `.putString(key, value)` stages a change. `.apply()` saves it asynchronously to disk.

### Loading It Back

```kotlin
private fun loadSqrtMode(context: Context): SqrtMode {
    val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
    return when (prefs.getString(SQRT_MODE_KEY, SqrtMode.AskEveryTime.name)) {
        SqrtMode.WrapCurrentInput.name -> SqrtMode.WrapCurrentInput
        SqrtMode.AppendToEnd.name      -> SqrtMode.AppendToEnd
        else                           -> SqrtMode.AskEveryTime
    }
}
```

`prefs.getString(key, defaultValue)` reads the stored string, falling back to the default if the key doesn't exist yet (first launch). The stored string is the enum's `.name` property (e.g., `"WrapCurrentInput"`), which is converted back to the enum value via the `when` expression.

### What is `Context`?

You'll see `Context` everywhere in Android. It is an object that provides access to application resources and system services — things like SharedPreferences, the camera, or the clipboard. Activities are a type of Context. In Compose, you get the current context with:

```kotlin
val context = LocalContext.current
```

> **Key term — Context:** Android's gateway to the system. Almost every Android API requires a Context to know *which app* is making the request.

---

## 16. Beyond the App — AppTileService

This is an advanced feature: a **Quick Settings tile** that appears in the pull-down notification shade and lets users open the calculator directly.

```kotlin
class AppTileService : TileService() {

    override fun onClick() {
        super.onClick()

        unlockAndRun {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                // API 34+ (Android 14+): startActivityAndCollapse(Intent) throws
                // UnsupportedOperationException — must use the PendingIntent overload instead.
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                startActivityAndCollapse(pendingIntent)
            } else {
                // API 24–33: the Intent overload still works fine.
                @Suppress("DEPRECATION")
                startActivityAndCollapse(intent)
            }
        }
    }
}
```

### Breaking It Down

- **`TileService`** — The Android base class for Quick Settings tiles. You override `onClick()` to respond when the tile is tapped.
- **`unlockAndRun { ... }`** — Waits for the device to be unlocked before running. Without this, tapping the tile from the lock screen would fail.
- **`Intent(this, MainActivity::class.java)`** — Creates an **explicit Intent** that directly names its target (`MainActivity`). This is more reliable than asking the package manager to look up a launch Intent, and can never return null.
- **`Intent.FLAG_ACTIVITY_NEW_TASK`** — Required when starting an Activity from outside an existing Activity context (which a Service is).
- **`Intent.FLAG_ACTIVITY_CLEAR_TOP`** — If the calculator is already open in the background, bring it to the front instead of launching a second copy.
- **`Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE`** — Checks whether the device is running **Android 14 (API 34)** or higher. `UPSIDE_DOWN_CAKE` is Android 14's internal codename. API checks like this are the standard way to use newer features while still supporting older devices.
- **`PendingIntent.getActivity(...)`** — Wraps the Intent in a `PendingIntent` — a token the system can hold on to and fire on the app's behalf at a later time. Required on API 34+ (see note below).
- **`PendingIntent.FLAG_IMMUTABLE`** — Declares that the system may not modify this PendingIntent's extras. Required for security on API 31+.
- **`PendingIntent.FLAG_UPDATE_CURRENT`** — If a matching PendingIntent already exists, update its extras with the new ones rather than failing.
- **`startActivityAndCollapse(pendingIntent)`** — The API 34+ version: opens the app via the PendingIntent and collapses the Quick Settings panel.
- **`@Suppress("DEPRECATION") startActivityAndCollapse(intent)`** — The pre-API-34 path. The `Intent` overload of `startActivityAndCollapse` still works fine on Android 13 and below. `@Suppress("DEPRECATION")` silences the lint warning that this overload is deprecated, since we know it's intentionally correct for this code path.

> **Why the API 34 split?**
> In **Android 14**, Google deprecated `startActivityAndCollapse(Intent)` and made it throw an `UnsupportedOperationException` if called — producing the "this app failed" error. The replacement `startActivityAndCollapse(PendingIntent)` did not exist before API 34. The `if/else` block handles both worlds: the correct new API on modern devices, and the still-working old API on older ones.

> **Key term — PendingIntent:** A wrapped Intent that grants another process (here, the Android system) the permission and ability to fire your Intent on your app's behalf at some point in the future. You'll also see PendingIntents used with Notifications and AlarmManager.

The service is registered in `AndroidManifest.xml` with a special permission:

```xml
<service
    android:name=".AppTileService"
    android:permission="android.permission.BIND_QUICK_SETTINGS_TILE"
    android:exported="true">
    <intent-filter>
        <action android:name="android.service.quicksettings.action.QS_TILE" />
    </intent-filter>
</service>
```

The `BIND_QUICK_SETTINGS_TILE` permission ensures only the Android system can bind to this service — no other app can hijack it.

---

## 17. The Big Picture — How It All Connects

Here is a complete map of how data and control flow through the entire app from a single button tap:

```
┌─────────────────────────────────────────────────────────┐
│                    AndroidManifest.xml                  │
│  Registers MainActivity as the LAUNCHER entry point     │
│  Registers AppTileService for Quick Settings            │
└──────────────────────────┬──────────────────────────────┘
                           │ Android OS starts
                           ↓
┌─────────────────────────────────────────────────────────┐
│                      MainActivity                        │
│  onCreate() → enableEdgeToEdge() → setContent {        │
│      CalculatorTheme {                                  │
│          CalculatorScreen()   ← Compose takes over      │
│      }                                                  │
│  }                                                      │
└──────────────────────────┬──────────────────────────────┘
                           │ Compose renders
                           ↓
┌─────────────────────────────────────────────────────────┐
│                    CalculatorScreen()                    │
│  Reads: vm.expression, vm.display (ViewModel state)    │
│  Reads: selectedTab, sqrtMode, pendingSqrtDialog       │
│           (local remember { } state)                   │
│                                                         │
│  Renders:  Header (pun + title + settings icon)        │
│            CalculatorTabContent  OR  UserSettingsTab   │
└──────────────────────────┬──────────────────────────────┘
                           │ user taps "3"
                           ↓
┌─────────────────────────────────────────────────────────┐
│               CalculatorButton onClick                   │
│  Calls vm.onKey("3")                                    │
└──────────────────────────┬──────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────┐
│               CalculatorViewModel.onKey("3")             │
│  expression = "3"   (mutableStateOf — triggers recomp) │
│  display    = "3"   (mutableStateOf — triggers recomp) │
└──────────────────────────┬──────────────────────────────┘
                           │ state changed
                           ↓
┌─────────────────────────────────────────────────────────┐
│            Compose Recomposition                         │
│  CalculatorScreen re-runs (only affected parts)        │
│  Display Text updates: "0" → "3"                       │
│  AnimatedContent fades old out, new in                  │
└─────────────────────────────────────────────────────────┘
```

### Files at a Glance

| File | Role |
|---|---|
| `AndroidManifest.xml` | App's declaration to the OS |
| `MainActivity.kt` | Entry point, lifecycle, UI bootstrap |
| `CalculatorViewModel` (in MainActivity.kt) | All calculator logic & state |
| `CalculatorScreen` (in MainActivity.kt) | Root UI composable |
| `CalculatorTabContent` | Calculator pad + display |
| `VariableChipRow` | Scrollable chip row for inserting variables and the `=` assignment operator |
| `VariableChip` | Single chip displaying a variable's name and its current value |
| `AssignEqualsChip` | The green `=` chip that inserts ` = ` into the expression |
| `UserSettingsTabContent` | Settings panel (sqrt mode + variable editor) |
| `VariableEditorRow` | Editable settings row for setting a named variable's value |
| `CalculatorButton` | Single reusable button component |
| `ButtonRow` | A row of buttons |
| `AppTileService.kt` | Quick Settings tile |
| `ui/theme/Color.kt` | All color constants |
| `ui/theme/Type.kt` | Typography definitions |
| `ui/theme/Theme.kt` | MaterialTheme wrapper |
| `res/values/strings.xml` | Text strings |
| `res/values/themes.xml` | XML-based theme (for launcher/system UI) |
| `build.gradle.kts` | Build configuration |
| `libs.versions.toml` | Dependency version catalog |

### The Concepts Ladder

Start here and climb:

```
1. Kotlin basics (val/var, fun, class, when, null safety)
         ↓
2. Android project structure (modules, res/, manifest)
         ↓
3. Gradle (how the project is compiled and packaged)
         ↓
4. Activity & lifecycle (what runs when)
         ↓
5. Jetpack Compose (declarative UI, @Composable, Modifier)
         ↓
6. Layout (Column, Row, Box, weight)
         ↓
7. State (mutableStateOf, remember, recomposition)
         ↓
8. ViewModel (persistence across config changes, UDF)
         ↓
9. Theming (MaterialTheme, colors, typography)
         ↓
10. Animation (animate*AsState, springs, tweens)
         ↓
11. Data persistence (SharedPreferences)
         ↓
12. User variables (AndroidViewModel, mutableStateMapOf, expression context, persistence)
         ↓
13. Services & system integration (TileService)
```

Every concept you learn opens the door to the next. This calculator app is a genuine, production-quality Android app — if you understand every piece of it, you have a solid foundation for building anything.

---

*Happy building. You can count on it.* 🧮
