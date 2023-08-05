package com.example.pharmashare

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.pharmashare.firebase.objects.Pharmacy
import com.example.pharmashare.firebase.repos.PharmacyRepository
import com.example.pharmashare.firebase.repos.UserRepository
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val create = findViewById<Button>(R.id.create)
        val textView = findViewById<TextView>(R.id.textView)

        // button create pharmacy
        create.setOnClickListener {
            val userid = UserRepository.getCurrentUserId()
            // retrieve pharmacies that belong to the current user
            PharmacyRepository.getAllPharmaciesByOwnerId(userid) { pharmacies ->
                // display pharmacies in the UI
                textView.text = pharmacies.toString()
                Log.d("ziad","ezzat")
                Log.d("pharmacies", pharmacies.toString())
            }
        }

    }
}