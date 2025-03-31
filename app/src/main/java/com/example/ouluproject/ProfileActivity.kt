package com.example.ouluproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity: AppCompatActivity(){

    private lateinit var imgBanner : ImageView
    private lateinit var imgProfile : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        // the back button
        val backButton = findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener{
            finish()
        } // end for button

        //logout button that returns to the login page.
        val logoutButton = findViewById<Button>(R.id.btn_logout)
        logoutButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }




        imgBanner = findViewById(R.id.img_banner)
        imgProfile = findViewById(R.id.img_profile)


        //let the user upload the banner and profile pic??

        imgBanner.setOnClickListener{
            bannerPicker.launch("image/*")
        }
        imgProfile.setOnClickListener{
            profilePicker.launch("image/*")
        }

        //This should show under basic info should also let the user edit and update
        val name = findViewById<EditText>(R.id.editName)
        val email = findViewById<EditText>(R.id.editEmail)
        val password = findViewById<EditText>(R.id.editPassword)

        //data from intent
        val RegisName = intent.getStringExtra("name")
        val RegisEmail = intent.getStringExtra("email")
        val RegisPassword = intent.getStringExtra("Password")

        //set values in fields
        name.setText(RegisName)
        email.setText(RegisEmail)
        password.setText(RegisPassword)


        //for the save button
        val saveButton = findViewById<Button>(R.id.btn_save)
        saveButton.setOnClickListener {
            val updatedName = name.text.toString().trim()
            val updatedEmail = email.text.toString().trim()
            val updatedPassword = password.text.toString().trim()

            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show()
        }



    }//end of on-create

    private val bannerPicker = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri: Uri? -> uri?.let { imgBanner.setImageURI(it) }
    }
    private val profilePicker = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri? -> uri?.let { imgProfile.setImageURI(it) }
    }







} //end


