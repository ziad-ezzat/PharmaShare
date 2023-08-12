package com.example.pharmashare.screens

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pharmashare.LoginActivity
import com.example.pharmashare.R
import com.example.pharmashare.databinding.ActivityHomeBinding
import com.example.pharmashare.firebase.repos.UserRepository

class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_frame, fragment)
                .commit()
        }

        binding.navMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_add_medicine -> {
                    val fragment = AddFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment).commit()
                }
                R.id.menu_profile -> {
                    val fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment).commit()
                }
                R.id.menu_home -> {
                    val fragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment).commit()
                }
                R.id.menu_order -> {
                    val fragment = CartFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment).commit()
                }
                R.id.main_Logout -> {
                    UserRepository.logout()
                    Intent(this, LoginActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            }
            true
        }
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

            R.id.main_Logout -> {
                UserRepository.logout()
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
        return true
    }

}