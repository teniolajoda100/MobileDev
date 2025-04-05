package com.example.ouluproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var imgBanner: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var switchPremium: Switch
    private lateinit var greetingText: TextView
    private lateinit var backButton: ImageView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)

        imgBanner = findViewById(R.id.img_banner)
        imgProfile = findViewById(R.id.img_profile)
        switchPremium = findViewById(R.id.switchPremium)
        greetingText = findViewById(R.id.tvGreeting)
        backButton = findViewById(R.id.btn_back)
        logoutButton = findViewById(R.id.btn_logout)

        val email = intent.getStringExtra("email") ?: ""
        val name = intent.getStringExtra("name") ?: dbHelper.getNameByEmail(email)

        val user = dbHelper.getUserByEmail(email)
        val isPremium = user["premium"]?.toString() == "1"

        greetingText.text = "Hi, $name"
        switchPremium.isChecked = isPremium

        switchPremium.setOnCheckedChangeListener { _, isChecked ->
            dbHelper.updatePremiumStatus(email, if (isChecked) 1 else 0)
            Toast.makeText(this,
                if (isChecked) "You're now a Premium user!" else "Premium removed.",
                Toast.LENGTH_SHORT
            ).show()
        }

        backButton.setOnClickListener {
            finish()
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        imgBanner.setOnClickListener {
            bannerPicker.launch("image/*")
        }

        imgProfile.setOnClickListener {
            profilePicker.launch("image/*")
        }
    }

    private val bannerPicker = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uri: Uri? -> uri?.let { imgBanner.setImageURI(it) }
    }

    private val profilePicker = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uri: Uri? -> uri?.let { imgProfile.setImageURI(it) }
    }
}
