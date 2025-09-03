package com.example.multi_llm_chat_bot.SCREEN

import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multi_llm_chat_bot.LocalStorage.Conversation
import com.example.multi_llm_chat_bot.chatBotVeiwModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractionScreen(viewModel: chatBotVeiwModel) {
    val models = listOf<String>("deepseek/deepseek-chat-v3-0324:free","deepseek/deepseek-r1:free","z-ai/glm-4.5-air:free","qwen/qwen3-coder:free","google/gemini-2.0-flash-exp:free","microsoft/mai-ds-r1:free","meta-llama/llama-3.3-70b-instruct:free","openai/gpt-oss-20b:free","google/gemma-3-27b-it:free")
    val state by viewModel.state.collectAsState() // Use 'by' for delegation
    var currentModel by remember { mutableStateOf("deepseek/deepseek-chat-v3-0324:free") }
    var question by remember { mutableStateOf("") }
    var ShownQuestion by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var conversationList : List<Conversation> =viewModel.getAllConversation().observeAsState(emptyList()).value

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text("conversations", modifier = Modifier.padding(16.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                if(conversationList.isEmpty()){
                    Text("No conversations found", modifier = Modifier.padding(16.dp))
                }
                else{
                    conversationList.forEach {
                        Text(it.title, modifier = Modifier.padding(16.dp).clickable(onClick = { /*Todo*/ }) ,)
                    }

                }

            }

        }

    ){
        Scaffold(
            modifier = Modifier
                .imePadding()
                .background(color = Color(9, 9, 9)), // Add imePadding to the Scaffold

////
////        topBar = {


////            TopAppBar(
////                title = { Text(currentModel) },
////                colors = TopAppBarDefaults.topAppBarColors(
////                    containerColor = Color(0xFF494848), // A light gray for example
////                    titleContentColor = Color.Black
////                )
////            )
//            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                models.forEach {
//                    DropdownMenuItem(
//                        text = { Text(it) },
//                        onClick = {
//                            currentModel = it.toString()
//                            expanded = false
//                        }
//                    )
//                }
//            }
////        },
////        bottomBar = {
////            Card(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(horizontal = 8.dp, vertical = 4.dp), // Add some padding
////                shape = RoundedCornerShape(24.dp), // Rounded corners for the card
////                elevation = CardDefaults.cardElevation(4.dp)
////            ) {
////                Box(
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(horizontal = 8.dp, vertical = 4.dp), // Padding inside the card
////                    contentAlignment = Alignment.Center
////                ) {
////                    OutlinedTextField(
////                        modifier = Modifier.fillMaxWidth(),
////                        value = question,
////                        onValueChange = { question = it },
////                        placeholder = { Text(text = "Type a question...") },
////                        trailingIcon = {
////                            IconButton(onClick = {
////                                // TODO: Implement send action
////                                // viewModel.sendMessage(question, currentModel)
////                                viewModel.getAnswer(currentModel, question)
////                                ShownQuestion = question
////                                question = "" // Clear input after sending
////                            }) {
////                                Icon(
////                                    imageVector = Icons.Default.Send,
////                                    contentDescription = "Send"
////                                )
////                            }
////                        },
////                        shape = RoundedCornerShape(24.dp) // Match card shape
////                    )
////                }
////            }
////        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                scope.launch {
//                    if (drawerState.isClosed) {
//                        drawerState.open()
//                    } else {
//                        drawerState.close()
//                    }
//                }
//
//            }){Icon(imageVector = Icons.AutoMirrored.Default.List, contentDescription = "Send")}
//        }
        )

        { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(10, 10, 10))) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxWidth(0.13f)
                        .padding(horizontal = 8.dp, vertical = 4.dp), // Add some padding
                    //shape = RoundedCornerShape(24.dp), // Rounded corners for the card
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp), // Padding inside the card
                        contentAlignment = Alignment.Center
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = currentModel.toString(),
                            readOnly = true,
                            onValueChange = {  },
                            // readOnly = true,
                            //placeholder = { Text(text = "Type a question...") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    // TODO: Implement send action
                                    // viewModel.sendMessage(question, currentModel)
//                                viewModel.getAnswer(currentModel, question)
//                                ShownQuestion = question
//                                question = "" // Clear input after sending
                                    expanded = !expanded

                                }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Send"
                                    )
                                }
                            },
                            //shape = RoundedCornerShape(24.dp) // Match card shape
                        )
                    }
                }
                Box(modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .padding(5.dp)
                    .background(
                        Color(
                            35,
                            35,
                            42,
                            255
                        ),

                        ), contentAlignment = Alignment.TopCenter
                ) {
                    LazyColumn(
                        modifier = Modifier

                            .padding(5.dp) // Apply padding from Scaffold
                            .padding(horizontal = 16.dp, vertical = 8.dp) // Additional padding for content
                    ) {
                        item {
                            Text(
                                text = "Question : $ShownQuestion",
                                modifier = Modifier.background(Color.LightGray),
                                color = Color.White,
                                fontSize = 20.sp,
                                //fontSize = 10.dp,
                                //fontStyle = FontStyle.Normal,

                                textAlign = TextAlign.Right
                            )
                        }
                        item { Spacer(modifier = Modifier.height(4.dp)) }
                        item { HorizontalDivider(modifier = Modifier.height(3.dp),color = Color.White) }
                        item { Spacer(modifier = Modifier.height(4.dp)) }

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
                                    Text(text = state.error!!, color=Color(255, 71, 71, 255))
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
                        .fillMaxWidth(0.3f)
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

            }

//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp, vertical = 4.dp), // Add some padding
//            shape = RoundedCornerShape(24.dp), // Rounded corners for the card
//            elevation = CardDefaults.cardElevation(4.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp, vertical = 4.dp), // Padding inside the card
//                contentAlignment = Alignment.Center
//            ) {
//                OutlinedTextField(
//                    modifier = Modifier.fillMaxWidth(),
//                    value = question,
//                    onValueChange = { question = it },
//                    placeholder = { Text(text = "Type a question...") },
//                    trailingIcon = {
//                        IconButton(onClick = {
//                            // TODO: Implement send action
//                            // viewModel.sendMessage(question, currentModel)
//                            viewModel.getAnswer(currentModel,question)
//                            ShownQuestion=question
//                            question = "" // Clear input after sending
//                        }) {
//                            Icon(
//                                imageVector = Icons.Default.Send,
//                                contentDescription = "Send"
//                            )
//                        }
//                    },
//                    shape = RoundedCornerShape(24.dp) // Match card shape
//                )
//            }
//        }
        }
    }



     //val models = listOf<String>("deepseek/deepseek-chat-v3-0324:free","deepseek/deepseek-r1:free","z-ai/glm-4.5-air:free","qwen/qwen3-coder:free","google/gemini-2.0-flash-exp:free","microsoft/mai-ds-r1:free","meta-llama/llama-3.3-70b-instruct:free","openai/gpt-oss-20b:free","google/gemma-3-27b-it:free") // Unused

   }

