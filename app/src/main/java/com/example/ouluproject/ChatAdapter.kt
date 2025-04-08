package com.example.ouluproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val messages = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.messageTextView.text = messages[position]
        // Check if the message is from the user or chatbot
        if (messages[position].startsWith("You:")) {
            holder.messageLayout.setBackgroundResource(R.drawable.user_message_bubble)
        } else {
            holder.messageLayout.setBackgroundResource(R.drawable.chatbot_message_bubble)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(message: String) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val messageLayout: LinearLayout = view.findViewById(R.id.messageLayout)
    }
}
