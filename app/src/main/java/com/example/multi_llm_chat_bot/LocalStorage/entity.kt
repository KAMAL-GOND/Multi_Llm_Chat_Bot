package com.example.multi_llm_chat_bot.LocalStorage

import android.icu.text.CaseMap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    var conversationId : Long= 0L,
    var title: String,
    var startTimeMillis :Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["conversationId"],
            childColumns = ["messageConversationId"],
            onDelete = ForeignKey.CASCADE
        )

    ],
    indices = [Index(value = ["messageConversationId"])])
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val messageId : Long =0L,
    val messageConversationId:Long,
    val question:String,
    val answer:String,
    val timestampMillis:Long = System.currentTimeMillis()

)

data class ConversationWithMessages(
    @Embedded val conversation: Conversation,
    @Relation(
        parentColumn = "conversationId", // From Conversation entity
        entityColumn = "messageConversationId"  // From ChatMessage entity
    )
    val messages: List<ChatMessage>
)