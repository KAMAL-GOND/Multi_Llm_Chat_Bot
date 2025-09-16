package com.example.multi_llm_chat_bot

//import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multi_llm_chat_bot.LocalStorage.AppDatabase
import com.example.multi_llm_chat_bot.LocalStorage.ChatMessage
import com.example.multi_llm_chat_bot.LocalStorage.Conversation
import com.example.multi_llm_chat_bot.Model.OpenRouterResponse
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

//import com.google.mediapipe.tasks.components.containers.NormalizedLandmarkList



class chatBotVeiwModel(db: AppDatabase, context: Context) : ViewModel() {
    private var llmInference: LlmInference? = null
    init {
        Log.d("ChatBotViewModel", "ViewModel initialized")
        viewModelScope.launch {
            try{

                val taskOptions = LlmInference.LlmInferenceOptions.builder()
                    .setModelPath("/data/local/tmp/llm/gemma.task")
                    .setMaxTopK(64)
                    .build()
                Log.d("ChatBotViewModel", "Task options set")
                llmInference = LlmInference.createFromOptions(context, taskOptions)
                Log.d("ChatBotViewModel", "LlmInference created")


            } catch(e: Exception) {
            Log.e("ChatBotViewModel", "Error creating LlmInference: ${e.message}")
        }}
    }
    var chatDao = db.chatDao()

    // Using a repository pattern is generally better, but for this fix, we'll keep direct DAO access.

    val _state= MutableStateFlow(appState(
        isLoading = false,
        response = null,
        error = null
        //ideal = true
    ))
    val state= _state.asStateFlow()
    var active=kotlinx.coroutines.Job(null)

    fun getAnswer(model : String , question : String, conversationId : Long) {
        if( model=="Gemma:2B"){
            viewModelScope.launch (){ // Assign to 'active' Job for potential cancellation if needed
                _state.update {
                    it.copy(isLoading = true)
                }

                while (true) { // This loop needs a better exit condition, or it will retry indefinitely
                    try {
                        var answer = withContext(Dispatchers.IO){llmInference?.generateResponse(question)}
                        Log.d("ChatBotViewModel", "Answer: $answer")

                        // Ensure message is saved with the correct conversationId
                        chatDao.insertChatMessage(
                            ChatMessage(
                                messageConversationId = conversationId,
                                question = question,
                                answer = answer.toString()
                               // answer = "test"
                            )
                        )
                        _state.update {
                            it.copy(
                                isLoading = false,
                                response = answer,
                                error = null
                            )
                        }
                        break // Exit loop on success

                    } catch (e: Exception) {
                            Log.e("ChatBotViewModel", "Error getting answer: ${e.message}")
                            _state.update { it.copy(error = e.message, isLoading = false) }
                        }
                }
        }}
        else{

        //active =
            viewModelScope.launch { // Assign to 'active' Job for potential cancellation if needed
                _state.update {
                    it.copy(isLoading = true)
                }
                while (true) { // This loop needs a better exit condition, or it will retry indefinitely
                    try {
                        var answer = KtorClient.request(model, question)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                response = answer,
                                error = null
                            )
                        }
                        // Ensure message is saved with the correct conversationId
                        chatDao.insertChatMessage(
                            ChatMessage(
                                messageConversationId = conversationId,
                                question = question,
                                answer = answer.choices?.get(0)?.message?.content.toString()
                            )
                        )
                        break // Exit loop on success
                    } catch (e: Exception) {
                        Log.e("ChatBotViewModel", "Error getting answer: ${e.message}")
                        _state.update { it.copy(error = e.message, isLoading = false) }
                    }
                    // Only retry if we actually want to. For now, commenting out the delay to avoid infinite retries.
                    // delay(60000)
                }
            }}
    }

    fun getAllConversation() = chatDao.getAllConversations()

    // Made suspend fun and returns the actual ID from the database
    suspend fun insertConversation(conversation: Conversation) : Long {
        return chatDao.insertConversation(conversation)
    }

    // Removed @Composable annotation
    fun getMessages(conversationId: Long): LiveData<List<ChatMessage>> {
        return chatDao.getMessagesForConversation(conversationId)
    }
//    private fun copyModelFromAssetsToCache(context: Context, modelName: String): String {
//        val modelFile = File(context.cacheDir, modelName)
//        if (!modelFile.exists()) {
//            Log.d("ChatViewModel", "Model not in cache, copying from assets...")
//            context.assets.open(modelName).use { inputStream ->
//                FileOutputStream(modelFile).use { outputStream ->
//                    inputStream.copyTo(outputStream)
//                }
//            }
//        }
//        return modelFile.absolutePath
//    }








}


data class appState(
    var isLoading:Boolean=false,
    var response : Any?=null,
    var error:String?=null,
    // var ideal: Boolean=true
)

data class State(
    var isLoading:Boolean=false,
    var response : Any?=null,
    var error:String?=null,
    // var ideal: Boolean=true
)



