package com.example.pharmashare.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pharmashare.R
import com.example.pharmashare.app.fragments.AddFragment
import com.example.pharmashare.app.fragments.CartFragment
import com.example.pharmashare.app.fragments.HomeFragment
import com.example.pharmashare.app.fragments.ProfileFragment
import com.example.pharmashare.databinding.ActivityHomeBinding
import com.example.pharmashare.database.firebase.repos.UserRepository

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

        registerForContextMenu(binding.imageView)
        binding.imageView.setOnClickListener{
            openContextMenu(it)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.main_menu,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.main_AddPharmacy->{
                Intent(this, AddPharmacy::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
            R.id.main_Logout->{
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes") { _, _ ->
                        UserRepository.logout()
                        Intent(this, LoginActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
        return true
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_AddPharmacy -> {
                Intent(this, AddPharmacy::class.java).also {
                    startActivity(it)
                    finish()
                }
            }

            R.id.main_Logout -> {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes") { _, _ ->
                        UserRepository.logout()
                        Intent(this, LoginActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
        return true
    }
}