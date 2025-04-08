package com.example.ouluproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatInput: EditText
    private lateinit var sendMessageButton: Button
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatInput = findViewById(R.id.chatInput)
        sendMessageButton = findViewById(R.id.sendMessageButton)
        backButton = findViewById(R.id.backButton)

        val chatAdapter = ChatAdapter()
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        sendMessageButton.setOnClickListener {
            val message = chatInput.text.toString()
            if (message.isNotEmpty()) {
                chatAdapter.addMessage("You: $message")
                chatInput.text.clear()

                // Simulate dynamic response
                val response = "ChatBot: Simulated response to '$message'"
                chatAdapter.addMessage(response)
            }
        }

        // Handle the back button click
        backButton.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
        }
    }
}
