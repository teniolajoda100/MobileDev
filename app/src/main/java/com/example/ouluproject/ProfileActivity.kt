package com.example.ouluproject

import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.ImageView
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


        imgBanner = findViewById(R.id.img_banner)
        imgProfile = findViewById(R.id.img_profile)


        //let the user upload the banner and profile pic??

        imgBanner.setOnClickListener{
            bannerPicker.launch("image/*")
        }

        imgProfile.setOnClickListener{
            profilePicker.launch("image/*")
        }
    }

    private val bannerPicker = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri: Uri? -> uri?.let { imgBanner.setImageURI(it) }
    }


    private val profilePicker = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri? -> uri?.let { imgProfile.setImageURI(it) }
    }

} //end


