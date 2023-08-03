package com.example.pharmashare.screens

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pharmashare.R

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_profile -> {
                Toast.makeText(baseContext, "${item.title}", Toast.LENGTH_SHORT).show()
            }

            R.id.main_setting -> {
                Toast.makeText(baseContext, "${item.title}", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}