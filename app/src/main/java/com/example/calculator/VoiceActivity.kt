package com.example.calculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.AppBlack
import com.example.calculator.ui.theme.AppGreen
import com.example.calculator.ui.theme.AppWhite
import com.example.calculator.ui.theme.CalculatorTheme
import java.util.Locale

class VoiceActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var resultToSpeak by mutableStateOf<String?>(null)

    private val speechRecognizerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
            setContent {
                CalculatorTheme {
                    VoiceResultScreen(spokenText, onDismiss = { finish() })
                }
            }
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)

        val query = intent.getStringExtra(RecognizerIntent.EXTRA_RESULTS)
            ?: intent.getStringExtra("query")
            
        if (query != null) {
            setContent {
                CalculatorTheme {
                    VoiceResultScreen(query, onDismiss = { finish() })
                }
            }
        } else if (intent.getBooleanExtra("trigger_speech", false)) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Solve an equation...")
            }
            speechRecognizerLauncher.launch(intent)
        } else {
            // Check for Assistant intent
            val assistantQuery = intent.getStringExtra("query") ?: ""
            setContent {
                CalculatorTheme {
                    VoiceResultScreen(assistantQuery, onDismiss = { finish() })
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            resultToSpeak?.let { speakResult(it) }
        }
    }

    private fun speakResult(text: String) {
        if (text != "Error" && text != "No equation heard") {
            tts?.speak("The result is $text", TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    @Composable
    fun VoiceResultScreen(query: String, onDismiss: () -> Unit) {
        val context = LocalContext.current
        val variables = remember {
            val prefs = context.getSharedPreferences("user_vars_prefs", Context.MODE_PRIVATE)
            val map = mutableMapOf<String, Double?>()
            (1..10).forEach { i ->
                val name = "var$i"
                map[name] = prefs.getString(name, null)?.toDoubleOrNull()
            }
            map["ans"] = prefs.getString("ans", null)?.toDoubleOrNull()
            map
        }
        
        val shortcuts = remember {
            val prefs = context.getSharedPreferences("user_shortcuts_prefs", Context.MODE_PRIVATE)
            val map = mutableMapOf<String, String>()
            prefs.all.forEach { (key, value) ->
                if (value is String) map[key] = value
            }
            map
        }

        val result = remember(query) {
            if (query.isBlank()) "No equation heard"
            else CalculatorEngine.safeEvaluate(query, variables, shortcuts)
        }
        
        LaunchedEffect(result) {
            if (tts != null) {
                speakResult(result)
            } else {
                resultToSpeak = result
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppWhite)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Voice Result", color = AppBlack, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                
                Text(
                    text = if (query.isBlank()) "Waiting for input..." else "\"$query\"",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Text(
                    text = result,
                    color = AppBlack,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = AppGreen)
                ) {
                    Text("Close", color = AppWhite)
                }
            }
        }
    }
}
