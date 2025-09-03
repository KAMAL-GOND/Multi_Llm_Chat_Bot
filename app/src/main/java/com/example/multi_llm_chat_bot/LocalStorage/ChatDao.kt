package com.example.multi_llm_chat_bot.LocalStorage

import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation):Long

    @Query("SELECT * FROM conversations order by startTimeMillis DESC")
    fun getAllConversations(): LiveData<List<Conversation>>


    @Query("SELECT * FROM conversations WHERE conversationId = :id")
    suspend fun getConversationById(id: Long): Conversation?

    @Delete
    suspend fun deleteConversation(conversation: Conversation)

    @Query("DELETE FROM conversations WHERE conversationId = :conversationId")
    suspend fun deleteConversationById(conversationId: Long)

    @Query("DELETE FROM conversations")
    suspend fun clearAllConversations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(chatMessage: ChatMessage):Long

    @Query("SELECT * FROM chat_messages WHERE messageConversationId = :conversationId ORDER BY timestampMillis ASC")
    fun getMessagesForConversation(conversationId: Long): LiveData<List<ChatMessage>>


    @Delete
    suspend fun deleteChatMessage(chatMessage: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE messageConversationId = :conversationId")
    suspend fun clearMessagesForConversation(conversationId: Long)


    // --- Operations with Relationships ---

    @Transaction // Ensures the query is performed atomically
    @Query("SELECT * FROM conversations WHERE conversationId = :conversationId")
    suspend fun getConversationWithMessages(conversationId: Long): ConversationWithMessages?

    @Transaction
    @Query("SELECT * FROM conversations ORDER BY startTimeMillis DESC")
    fun getAllConversationsWithMessages(): LiveData<List<ConversationWithMessages>> // Or Flow<List<ConversationWithMessages>>

    @Transaction
    @Query("SELECT * FROM conversations WHERE conversationId = :conversationId")
    suspend fun getConversationWithMessagesById(conversationId: Long): ConversationWithMessages?



}