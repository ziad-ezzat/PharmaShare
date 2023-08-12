package com.example.pharmashare.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.pharmashare.R
import com.example.pharmashare.database.objects.Pharmacy
import com.example.pharmashare.database.objects.User
import com.example.pharmashare.database.firebase.repos.UserRepository

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextPharmacyName: EditText
    private lateinit var editTextPharmacyAddress: EditText
    private lateinit var buttonRegister: Button
    private lateinit var textViewError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextPharmacyName = findViewById(R.id.editTextPharmacyName)
        editTextPharmacyAddress = findViewById(R.id.editTextPharmacyAddress)
        buttonRegister = findViewById(R.id.buttonRegister)
        textViewError = findViewById(R.id.textViewError)

        buttonRegister.setOnClickListener {
            registerUser()
        }

        textViewError.visibility = View.GONE
    }

    private fun showErrorMessage(message: String) {
        textViewError.visibility = View.VISIBLE
        textViewError.text = message
    }

    private fun registerUser() {
        val username = editTextUsername.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()
        val phoneNumber = editTextPhoneNumber.text.toString().trim()
        val pharmacyName = editTextPharmacyName.text.toString().trim()
        val pharmacyAddress = editTextPharmacyAddress.text.toString().trim()

        if (username.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() ||
            confirmPassword.isEmpty() || pharmacyName.isEmpty() || pharmacyAddress.isEmpty()) {
            showErrorMessage("Please fill in all fields")
            return
        }

        if (password != confirmPassword) {
            showErrorMessage("Passwords do not match")
            return
        }

        // create user hava a pharmacy
        val user = User("Temp",username,phoneNumber,password,"Temp")
        val pharmacy = Pharmacy("Temp",pharmacyName,pharmacyAddress,"Temp")

        UserRepository.createUserAndPharmacy(user,pharmacy) { success, message ->
            if (success) {
                // Navigate to the next activity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}