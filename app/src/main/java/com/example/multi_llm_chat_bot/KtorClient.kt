package com.example.multi_llm_chat_bot

import android.util.Log
import com.example.multi_llm_chat_bot.Model.OpenRouterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

object KtorClient {
    val apiKey= Apikey
    val httpClient = HttpClient(CIO) {
        expectSuccess=true;
        install(ContentNegotiation){
            json(json= Json { ignoreUnknownKeys=true })
        }
        install(Logging){
            logger=Logger.SIMPLE
            level= LogLevel.ALL
        }
        install(HttpTimeout){
            requestTimeoutMillis=60000
            connectTimeoutMillis=60000
            socketTimeoutMillis=60000
        }

    }

    suspend fun request(model : String , question : String) : OpenRouterResponse{
        Log.d("request","$model $question")
        val response =httpClient.post( "https://openrouter.ai/api/v1/chat/completions" ){
            header("Content-Type","application/json")
            header("Authorization","Bearer $apiKey")
            setBody(
                buildJsonObject {
                    put("model", model.toString())
                    putJsonArray("messages") {
                        addJsonObject {
                            put("role", "user")
                            put("content", "$question")
                        }
                    }
                }
            )

        }
        Log.d("response",response.bodyAsText())
        return response.body<OpenRouterResponse>()

    }
}