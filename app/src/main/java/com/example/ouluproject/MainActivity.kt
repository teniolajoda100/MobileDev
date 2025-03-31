package com.example.ouluproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        //val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
           // changed val username = etUsername.text.toString().trim() ->
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()


            //changed if (username.isEmpty() || password.isEmpty()) ->
            if (email.isEmpty() || password.isEmpty()) {
                // changed Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {

                //changed if (dbHelper.validateUser(username, password)) to ->
                if (dbHelper.validateUser(email, password)) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Homepage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
