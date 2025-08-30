package com.example.multi_llm_chat_bot.SCREEN

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multi_llm_chat_bot.chatBotVeiwModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractionScreen(viewModel: chatBotVeiwModel) {
    val state by viewModel.state.collectAsState() // Use 'by' for delegation
    var currentModel by remember { mutableStateOf("deepseek/deepseek-chat-v3-0324:free") }
    var question by remember { mutableStateOf("") }
    var ShownQuestion by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }


     val models = listOf<String>("deepseek/deepseek-chat-v3-0324:free","deepseek/deepseek-r1:free","z-ai/glm-4.5-air:free","qwen/qwen3-coder:free","google/gemini-2.0-flash-exp:free","microsoft/mai-ds-r1:free","meta-llama/llama-3.3-70b-instruct:free","openai/gpt-oss-20b:free","google/gemma-3-27b-it:free") // Unused

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentModel) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF0F0F0), // A light gray for example
                    titleContentColor = Color.Black
                )
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                models.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            currentModel = it.toString()
                            expanded = false
                        }
                    )
                }
            }
        },
        bottomBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp), // Add some padding
                shape = RoundedCornerShape(24.dp), // Rounded corners for the card
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp), // Padding inside the card
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = question,
                        onValueChange = { question = it },
                        placeholder = { Text(text = "Type a question...") },
                        trailingIcon = {
                            IconButton(onClick = {
                                // TODO: Implement send action
                                // viewModel.sendMessage(question, currentModel)
                                viewModel.getAnswer(currentModel, question)
                                ShownQuestion = question
                                question = "" // Clear input after sending
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send"
                                )
                            }
                        },
                        shape = RoundedCornerShape(24.dp) // Match card shape
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                expanded = !expanded

            }){Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Send")}
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(innerPadding).background(Color(
            35,
            35,
            42,
            255
        )
        )) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding from Scaffold
                .padding(horizontal = 16.dp, vertical = 8.dp) // Additional padding for content
        ) {
            item {
                Text(
                    text = "Question : $ShownQuestion",
                    modifier = Modifier,
                    color = Color.White,
                    fontSize = 20.sp,
                    //fontSize = 10.dp,
                    //fontStyle = FontStyle.Normal,

                    textAlign = TextAlign.Right
                )
            }
            when {
                state.isLoading -> {
                    item {
                        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }

                state.response != null -> {
                    item {
                        Text(text = state.response!!.choices?.get(0)!!.message!!.content!!.toString(),
                           // text = "Question : $ShownQuestion",
                            modifier = Modifier,
                            color = Color.White,
                            fontSize = 20.sp,
                            //fontSize = 10.dp,
                            //fontStyle = FontStyle.Normal,

                            textAlign = TextAlign.Left)
                    }
                }

                state.error != null -> {
                    item {
                        Text(text = state.error!!)
                    }
                }

            }

            // items(state.messages) { message ->
            //    Text(text = message.content)
            // }
        }
    }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp), // Add some padding
            shape = RoundedCornerShape(24.dp), // Rounded corners for the card
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp), // Padding inside the card
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = question,
                    onValueChange = { question = it },
                    placeholder = { Text(text = "Type a question...") },
                    trailingIcon = {
                        IconButton(onClick = {
                            // TODO: Implement send action
                            // viewModel.sendMessage(question, currentModel)
                            viewModel.getAnswer(currentModel,question)
                            ShownQuestion=question
                            question = "" // Clear input after sending
                        }) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send"
                            )
                        }
                    },
                    shape = RoundedCornerShape(24.dp) // Match card shape
                )
            }
        }
    }
}
