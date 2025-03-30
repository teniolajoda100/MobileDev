package com.example.ouluproject

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        //back button to go back home
        val backButton = findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener{
            finish() // this closes and returns to the main page
        }
    }
}

