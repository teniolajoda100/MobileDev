package com.example.ouluproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatInput: EditText
    private lateinit var sendMessageButton: Button
    private lateinit var backButton: ImageView
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatInput = findViewById(R.id.chatInput)
        sendMessageButton = findViewById(R.id.sendMessageButton)
        backButton = findViewById(R.id.backButton)

        chatAdapter = ChatAdapter()
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        sendMessageButton.setOnClickListener {
            val message = chatInput.text.toString()
            if (message.isNotEmpty()) {
                // Display user message in chat
                chatAdapter.addMessage("You: $message")
                chatInput.text.clear()

                // Send the message to OpenAI API
                sendMessageToAPI(message)
            }
        }

        // Handle the back button click
        backButton.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun sendMessageToAPI(message: String) {
        // Create the request body
        val messages = listOf(Message(role = "user", content = message))
        val request = ChatRequest(messages = messages)

        // Get API key from BuildConfig
        val apiKey = BuildConfig.OPENAI_API_KEY  // Make sure the key is stored correctly

        Log.d("API_KEY", apiKey)  // You can log the API key here for debugging purposes

        // Make sure Retrofit instance is created using the provided API key
        RetrofitInstance.getOpenAIService(apiKey).sendMessage(request).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    // Log the successful response for debugging
                    val aiResponse = response.body()?.choices?.firstOrNull()?.message?.content
                    aiResponse?.let {
                        // Display AI's response in chat
                        chatAdapter.addMessage("ChatBot: $it")
                    }
                } else {
                    // Log the error details from the response
                    val errorResponse = response.errorBody()?.string()
                    Log.e("ChatActivity", "Error Response: $errorResponse")
                    Log.e("ChatActivity", "Error Code: ${response.code()}")
                    // Show a generic error message
                    chatAdapter.addMessage("ChatBot: Error occurred. ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                Log.e("ChatActivity", "API Call Failed: ${t.message}", t)
                t.printStackTrace() // Print stack trace for detailed error information
                // Show a user-friendly error message.
                chatAdapter.addMessage("ChatBot: Failed to get response. Please try again.")
            }
        })
    }
}
