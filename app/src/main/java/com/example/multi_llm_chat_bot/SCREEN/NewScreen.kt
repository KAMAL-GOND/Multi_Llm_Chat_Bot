package com.example.multi_llm_chat_bot.SCREEN

//package com.example.multi_llm_chat_bot.SCREEN

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.multi_llm_chat_bot.LocalStorage.ChatMessage
import com.example.multi_llm_chat_bot.LocalStorage.Conversation
import com.example.multi_llm_chat_bot.R
import com.example.multi_llm_chat_bot.chatBotVeiwModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewInteractionScreen(viewModel: chatBotVeiwModel) {
    val models = listOf<String>("deepseek/deepseek-chat-v3-0324:free","deepseek/deepseek-r1:free","z-ai/glm-4.5-air:free","qwen/qwen3-coder:free","google/gemini-2.0-flash-exp:free","microsoft/mai-ds-r1:free","meta-llama/llama-3.3-70b-instruct:free","openai/gpt-oss-20b:free","google/gemma-3-27b-it:free")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    var question by remember { mutableStateOf("") }
    var currentModel by remember { mutableStateOf("google/gemini-2.0-flash-exp:free") } // Default model
    var ShownQuestion by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }


    val conversationList by viewModel.getAllConversation().observeAsState(emptyList())

    var thisConverastionId by remember { mutableStateOf(0L) }


    val messagesLiveData: LiveData<List<ChatMessage>> = remember(thisConverastionId) {
        viewModel.getMessages(thisConverastionId)
    }
    // Observe the messages for the current conversation
    val Realmessages by messagesLiveData.observeAsState(emptyList())

    // Automatically select the most recent conversation if starting with 0L
    LaunchedEffect(conversationList) {
        if (thisConverastionId == 0L && conversationList.isNotEmpty()) {
            // Select the most recent conversation (assuming first item is most recent)
            thisConverastionId = conversationList.first().conversationId.toLong()
            Log.d("InteractionScreen", "Auto-selected conversation ID: $thisConverastionId")
        }
    }


    val listState = rememberLazyListState()

    // Scroll to the bottom whenever a new message or state change occurs
    LaunchedEffect(Realmessages.size, state.isLoading, state.response, state.error) {
        if (Realmessages.isNotEmpty() || state.isLoading || state.response != null || state.error != null) {
            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                        .background(MaterialTheme.colorScheme.surface),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Models",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
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
                    HorizontalDivider()
                    TextField(
                        value = currentModel.toString(),
                        onValueChange = {  },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp).background(color=MaterialTheme.colorScheme.onSurface),
                        //readOnly = true,

                        trailingIcon = {
                            IconButton(onClick = {

                                expanded = !expanded

                            }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Send"
                                )
                            }
                        },





                    )
//                    Text(
//                        currentModel.toString(),
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .clickable {
//                                currentModel = "OpenRouter"
//                                scope.launch { drawerState.close() }
//                            },
//                        color = if (currentModel == "OpenRouter") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
//                    )
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))

                    // New Chat button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                thisConverastionId = 0L // Reset to 0 to indicate a new chat
                                scope.launch { drawerState.close() }
                            })
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "New Chat")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "New Chat",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Conversations",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider()

                    // Display all conversations
                    if (conversationList.isEmpty()) {
                        Text(
                            "No conversations yet.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        LazyColumn {
                            items(conversationList) { conversation ->
                                Text(
                                    conversation.title.toString().take(50),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = {
                                            thisConverastionId = conversation.conversationId.toLong()
                                            scope.launch { drawerState.close() }
                                        })
                                        .padding(16.dp),
                                    color = if (thisConverastionId == conversation.conversationId.toLong()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxWidth(0.13f)
//                        .padding(horizontal = 8.dp, vertical = 4.dp), // Add some padding
//                    //shape = RoundedCornerShape(24.dp), // Rounded corners for the card
//                    elevation = CardDefaults.cardElevation(4.dp)
//                ) {
//                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                        models.forEach {
//                            DropdownMenuItem(
//                                text = { Text(it) },
//                                onClick = {
//                                    currentModel = it.toString()
//                                    expanded = false
//                                }
//                            )
//                        }
//                    }
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 8.dp, vertical = 4.dp), // Padding inside the card
//                        contentAlignment = Alignment.Center
//                    ) {
//                        OutlinedTextField(
//                            modifier = Modifier.fillMaxWidth(),
//                            value = currentModel.toString(),
//                            readOnly = true,
//                            onValueChange = {  },
//                            // readOnly = true,
//                            //placeholder = { Text(text = "Type a question...") },
//                            trailingIcon = {
//                                IconButton(onClick = {
//
//                                    expanded = !expanded
//
//                                }) {
//                                    Icon(
//                                        imageVector = Icons.Default.KeyboardArrowDown,
//                                        contentDescription = "Send"
//                                    )
//                                }
//                            },
//                            //shape = RoundedCornerShape(24.dp) // Match card shape
//                        )
//                    }
//                }
                TopAppBar(
                    title = { Text("Multi-LLM Chat Bot") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Chat message display area
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Top,
                    //listState = listState
                ) {
                    items(Realmessages) { message ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // User Question Card
                            if (message.question.isNotBlank()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Card(
                                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                        elevation = CardDefaults.cardElevation(2.dp)
                                    ) {
                                        Text(
                                            text = message.question,
                                            modifier = Modifier.padding(12.dp),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                            // Bot Answer Card
                            if (message.answer.isNotBlank()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Card(
                                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                                        elevation = CardDefaults.cardElevation(2.dp)
                                    ) {
                                        Text(
                                            text = message.answer,
                                            modifier = Modifier.padding(12.dp),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Display pending question and AI response/loading after historical messages
                    if (ShownQuestion.isNotBlank() && state.isLoading) {
                        item {
                            // Display the user's pending question before the AI starts loading
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Card(
                                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Text(
                                        text = ShownQuestion,
                                        modifier = Modifier.padding(12.dp),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }

                    // Display loading indicator or latest AI response/error
                    if (state.isLoading || state.response != null || state.error != null) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                } else if (state.error != null) {
                                    Card(
                                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                        elevation = CardDefaults.cardElevation(2.dp)
                                    ) {
                                        Text(
                                            text = "Error: ${state.error}",
                                            modifier = Modifier.padding(12.dp),
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }}
//                                } else if (state.response != null) {
//                                    Card(
//                                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp),
//                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
//                                        elevation = CardDefaults.cardElevation(2.dp)
//                                    ) {
//                                        Text(
//                                            text = state.response!!.choices?.get(0)?.message?.content ?: "No content",
//                                            modifier = Modifier.padding(12.dp),
//                                            color = MaterialTheme.colorScheme.onSurface
//                                        )
//                                    }
//                                }
                            }
                        }
                    }
                }

                // Input field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message...") },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (question.isNotBlank()) {
                            ShownQuestion = question // Capture pending question for display
                            val currentQuestion = question // Capture current question for the coroutine
                            question = "" // Clear input immediately for better UX
                            scope.launch {
                                // Determine conversation ID: either existing or create a new one
                                val conversationIdToUse = if (thisConverastionId == 0L) {
                                    val newConversation = Conversation(title = currentQuestion.take(50)) // Use part of question for title
                                    val insertedId = viewModel.insertConversation(newConversation)
                                    thisConverastionId = insertedId // Update the state with the new conversation ID
                                    Log.d("InteractionScreen", "New conversation created with ID: $insertedId")
                                    insertedId
                                } else {
                                    Log.d("InteractionScreen", "Using existing conversation ID: $thisConverastionId")
                                    thisConverastionId
                                }
                                viewModel.getAnswer(currentModel, currentQuestion, conversationIdToUse)
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Default.Send, contentDescription = "Send Message")
                    }
                }
            }
        }
    }
}