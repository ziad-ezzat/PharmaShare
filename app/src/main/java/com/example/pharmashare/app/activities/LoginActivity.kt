package com.example.pharmashare.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pharmashare.R
import com.example.pharmashare.database.firebase.repos.UserRepository

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Get references to UI elements
        val phoneNumberEditText = findViewById<EditText>(R.id.editTextPhoneNumber)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val registerButton = findViewById<TextView>(R.id.textViewSignUp)

        // if user is already logged in, navigate to the next activity
        if (UserRepository.isLoggedIn()) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set click listener on login button
        loginButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if user entered phone number and password
            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter phone number and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if user exists in Firebase database
            UserRepository.checkLogin(phoneNumber, password) { success, message ->
                if (success) {
                    // Navigate to the next activity
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set click listener on register button
        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}