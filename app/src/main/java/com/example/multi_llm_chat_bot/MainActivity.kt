package com.example.multi_llm_chat_bot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.multi_llm_chat_bot.ui.theme.Multi_Llm_Chat_BotTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //val scope= CoroutineScope(Dispatchers.IO)
        setContent {
            Multi_Llm_Chat_BotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val scope= rememberCoroutineScope()
    var a= remember{mutableStateOf("")}
    LaunchedEffect(Unit) {
        scope.launch {
            a.value= KtorClient.request("deepseek/deepseek-chat-v3.1:free","write essay on kotlin in 50 words")?.choices?.get(0)!!.message!!.content!!.toString()
            println(a.value)
        }
    }
    Text(
        text = "Hello $a",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val scope= rememberCoroutineScope()
    var a= remember{mutableStateOf("")}
    LaunchedEffect(Unit) {
        scope.launch {
          // a.value= KtorClient.request("deepseek/deepseek-chat-v3.1:free","write essay on kotlin in 50 words")!!.choices[0]!!.message!!.content!!.toString()
            println(a.value)
        }
    }
    //var response=scope.launch() {KtorClient.request("deepseek/deepseek-chat-v3.1:free","write essay on kotlin in 50 words")}

    Multi_Llm_Chat_BotTheme {
        Greeting("$a")
    }
}