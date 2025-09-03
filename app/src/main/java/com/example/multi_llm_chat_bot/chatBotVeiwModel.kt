package com.example.multi_llm_chat_bot

//import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multi_llm_chat_bot.LocalStorage.AppDatabase
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
        fun getAnswer(model : String , question : String){

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
                        chatDao.insertConversation(Conversation(title = question))
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

    }
    data class appState(
        var isLoading:Boolean=false,
        var response : OpenRouterResponse?=null,
        var error:String?=null,
       // var ideal: Boolean=true
         )

