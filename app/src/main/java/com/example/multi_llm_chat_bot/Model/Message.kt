package com.example.multi_llm_chat_bot.Model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val content: String?,
    val reasoning: String?,
    //@Serializable(with = AnySerializer::class)
    val refusal: String?,
    val role: String?
)