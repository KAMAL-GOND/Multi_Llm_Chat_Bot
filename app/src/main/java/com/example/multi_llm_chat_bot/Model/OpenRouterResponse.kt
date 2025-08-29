package com.example.multi_llm_chat_bot.Model

import kotlinx.serialization.Serializable

@Serializable
data class OpenRouterResponse(
    val choices: List<Choice?>?,
    val created: Int?,
    val id: String?,
    val model: String?,
    val `object`: String?,
    val provider: String?,
    val usage: Usage?
)