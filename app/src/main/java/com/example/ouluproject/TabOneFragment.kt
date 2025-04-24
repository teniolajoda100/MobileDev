package com.example.ouluproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

private lateinit var dbHelper: DatabaseHelper
class TabOneFragment : Fragment() {

    //pfp and header upload
    private lateinit var imgBanner: ImageView
    private lateinit var imgProfile: ImageView

    //logout btn
    private lateinit var logoutButton: Button

    //premium
    private lateinit var switchPremium: Switch
    private lateinit var greetingText: TextView




    // File pickers for banner and profile
    private val bannerPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imgBanner.setImageURI(it) }
    }

    private val profilePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imgProfile.setImageURI(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        imgBanner = view.findViewById(R.id.img_banner)
        imgProfile = view.findViewById(R.id.img_profile)
        switchPremium = view.findViewById(R.id.switchPremium)
        greetingText = view.findViewById(R.id.tvGreeting)

        logoutButton = view.findViewById(R.id.btn_logout)



        val email = requireActivity().intent.getStringExtra("email") ?: ""
        val name = requireActivity().intent.getStringExtra("name") ?: dbHelper.getNameByEmail(email)
        val user = dbHelper.getUserByEmail(email)
        val isPremium = user["premium"]?.toString() == "1"

        greetingText.text = "Hi, $name"
        switchPremium.isChecked = isPremium

        //Upgrade the user
        switchPremium.setOnCheckedChangeListener { _, isChecked ->
            dbHelper.updatePremiumStatus(email, if (isChecked) 1 else 0)
            Toast.makeText(requireContext(),
                if (isChecked) "You're now a Premium user!" else "Premium removed.",
                Toast.LENGTH_SHORT
            ).show()
        }

        //showing user password
        val userPassword = view.findViewById<TextView>(R.id.userPass)
        val password = user["password"]?.toString() ?: ""
        val maskedPassword = "*".repeat(8)

        userPassword.text = "Password: $maskedPassword"


        //showing user email
        val userEmail = view.findViewById<TextView>(R.id.userEmail)
        userEmail.text = "Email: $email"
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)

        btnEditProfile.setOnClickListener {
            val editText = EditText(requireContext())
            editText.setText(email)

            //make the alert box prompt
            AlertDialog.Builder(requireContext())
                .setTitle("Edit Email")
                .setMessage("Enter your new email address")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    val newEmail = editText.text.toString().trim()
                    if (newEmail.isEmpty()) {
                        Toast.makeText(requireContext(), "Email can't be empty", Toast.LENGTH_SHORT).show()
                    } else {
                        val updated = dbHelper.updateEmail(email, newEmail)
                        if (updated) {
                            Toast.makeText(requireContext(), "Email updated!", Toast.LENGTH_SHORT).show()
                            userEmail.text = "Email: $newEmail"
                        } else {
                            Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }



        // Set onClickListeners to pick images
        imgBanner.setOnClickListener {
            bannerPicker.launch("image/*")
        }

        imgProfile.setOnClickListener {
            profilePicker.launch("image/*")
        }
        logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
        }

    }
}