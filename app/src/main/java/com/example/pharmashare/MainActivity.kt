package com.example.pharmashare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.pharmashare.database.AppDatabase
import com.example.pharmashare.database.entities.User
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // How to use the database in the app:
//        val db = AppDatabase.getInstance(this)
//
//        lifecycleScope.launch {
//            val userDao = db.userDao()
//            val user = User(2, "ziad", "9876543210","4321",2)
//            userDao.insert(user)
//        }

    }
}