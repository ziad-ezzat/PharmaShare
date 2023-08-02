package com.example.pharmashare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pharmashare.database.AppDatabase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // if user is already logged in, go to main activity
        val loggedInUser = AppDatabase.getInstance(this).userDao().getLoggedInUser()
        if (loggedInUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val phoneNumberEditText = findViewById<EditText>(R.id.editTextPhoneNumber)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        // when login button is clicked, check if credentials are valid
        loginButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val user = AppDatabase.getInstance(this@LoginActivity).userDao()
                    .checkLoginCredentials(phoneNumber, password)

                if (user != null) {
                    // if credentials are valid, set user as logged in
                    AppDatabase.getInstance(this@LoginActivity).userDao().setLoggedInUser(user.id)
                    // if credentials are valid, go to main activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // if credentials are invalid, show error message using Toast
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}