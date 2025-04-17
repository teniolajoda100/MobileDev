
package com.example.ouluproject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Data classes for the API request
data class ChatRequest(
    val model: String = "gpt-3.5-turbo",  // Use a valid model like gpt-3.5-turbo or gpt-4
    val messages: List<Message>
)

data class Message(
    val role: String,  // "user" or "assistant"
    val content: String
)

// Data class for the API response
data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

// Retrofit interface to interact with OpenAI API
interface OpenAIService {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")  // This is the correct endpoint
    fun sendMessage(@Body request: ChatRequest): Call<ChatResponse>  // The request is of type ChatRequest
}
