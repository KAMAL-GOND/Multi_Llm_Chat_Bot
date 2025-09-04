package com.example.multi_llm_chat_bot

//import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multi_llm_chat_bot.LocalStorage.AppDatabase
import com.example.multi_llm_chat_bot.LocalStorage.ChatMessage
import com.example.multi_llm_chat_bot.LocalStorage.Conversation
import com.example.multi_llm_chat_bot.Model.OpenRouterResponse
import io.ktor.http.ContentType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class chatBotVeiwModel(db: AppDatabase) : ViewModel() {
    var chatDao = db.chatDao()
    // var chatbot1= AppDatabase.getDatabase(this.)

        val _state= MutableStateFlow(appState(
            isLoading = false,
            response = null,
            error = null
            //ideal = true
        ))
        val state= _state.asStateFlow()
        var active=kotlinx.coroutines.Job(null)
        fun getAnswer(model : String , question : String,conversationId : Long){

            var active=viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true,)
                }
                while(true){
                    try {
                        var answer=KtorClient.request(model,question)
                        _state.update { it.copy(
                            isLoading = false,
                            response = answer ,
                            error = null
                        )

                        }
                        chatDao.insertChatMessage(ChatMessage(messageConversationId = conversationId, question = question, answer = answer.choices?.get(0)?.message?.content.toString()))
                        //chatDao.insertConversation(Conversation(title = question))
                        break
                    }
                    catch (e: Exception){
                        _state.update { it.copy(error= e.message, isLoading = false) }
                    }
                    delay(60000)
                }

            }



        }
    fun getAllConversation() = chatDao.getAllConversations()
    fun insertConversation(conversation: Conversation) : Long {
        var id = 0L
        viewModelScope.launch {
            id = chatDao.insertConversation(conversation)
        }
        return id
    }
    @Composable
    fun getMessages(conversationId: Long):androidx.lifecycle.LiveData<List<ChatMessage>> {
        var a =chatDao.getMessagesForConversation(conversationId)
        Log.d("check","message view model"+a.observeAsState().toString())
        return a
    }

    }


    data class appState(
        var isLoading:Boolean=false,
        var response : OpenRouterResponse?=null,
        var error:String?=null,
       // var ideal: Boolean=true
         )

