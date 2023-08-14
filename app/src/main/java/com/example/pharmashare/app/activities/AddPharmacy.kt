package com.example.pharmashare.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.pharmashare.R
import com.example.pharmashare.database.firebase.repos.PharmacyRepository

class AddPharmacy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pharmacy)

        val addPharmacyBtn = findViewById<Button>(R.id.buttonAddPharmacy)
        val spinnerCity = findViewById<Spinner>(R.id.spinnerCity)
        val pharmacyAddressDetails = findViewById<EditText>(R.id.PharmacyAddressIndetails)
        val pharmacyName = findViewById<EditText>(R.id.editTextPharmacyName)

        addPharmacyBtn.setOnClickListener {
            val pharmacyName = pharmacyName.text.toString().trim()
            val pharmacyAddress = pharmacyAddressDetails.text.toString().trim()

            if (pharmacyName.isEmpty() || pharmacyAddress.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedCity = spinnerCity.selectedItem.toString()
            if (selectedCity.isEmpty()) {
                Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fullAddress = "$selectedCity - $pharmacyAddress"
            PharmacyRepository.insertPharmacy(pharmacyName, fullAddress) { isSuccess ->
                if (isSuccess) {
                    Intent(this, HomeActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            }
        }
    }
}