package com.example.multi_llm_chat_bot.Model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val finish_reason: String?,
    val index: Int?,
    val logprobs: List<String>?,
    val message: Message?,
    val native_finish_reason: String?
)