package com.example.pharmashare.screens

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pharmashare.R
import com.example.pharmashare.databinding.ActivityHomeBinding
import com.example.pharmashare.firebase.objects.Medicine
import com.example.pharmashare.firebase.objects.Order
import com.example.pharmashare.firebase.objects.SharedMedicine
import com.example.pharmashare.screens.adptars.HomeAdaptar
import com.example.pharmashare.screens.adptars.OrderAdaptar
import com.example.pharmashare.screens.adptars.profileAdaptar
import kotlinx.coroutines.launch

class Home : AppCompatActivity(), HomeAdaptar.HomeLinsener {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    val arr1 = arrayListOf<Medicine>(
        Medicine("1", "test01"),
        Medicine("2", "test02"),
        Medicine("3", "test03"),
        Medicine("4", "test04"),
        Medicine("5", "test05")
    )
    val arr2 = arrayListOf<SharedMedicine>(
        SharedMedicine("1", "2", "1", 12, "10/8/2024", 15.0),
        SharedMedicine("2", "1", "2", 30, "10/8/2024", 4.0),
        SharedMedicine("3", "3", "3", 8, "10/8/2024", 32.0)
    )
    val arr3 = arrayListOf<Order>(
        Order("1", "2", "7/8/2023", 306.0, "ok01", "done"),
        Order("2", "2", "7/8/2023", 256.0, "ok02", "failed"),
        Order("3", "3", "7/8/2023", 466.0, "ok03", "on-process"),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val homeAdapter = HomeAdaptar(this)
        val orderAdptar = profileAdaptar()
        homeAdapter.arr = arr2
        homeAdapter.list = arr1
        orderAdptar.arrayList = arr3
        binding.navMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    println("home")
                    val fragment = home_fragment.newInstance(homeAdapter)
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment)
                        .commit()
                }
                R.id.menu_add_medicine -> {
                    println("add")
                    val fragment = AddFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment)
                        .commit()
                }
                R.id.menu_order -> {
                    println("order")

                }

                R.id.menu_profile -> {
                    println("profile")
                    val fragment = ProfileFragment.newInstance(1, orderAdptar)
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment)
                        .commit()
                }
            }
            return@setOnItemSelectedListener true
        }
        val homeAdptar = HomeAdaptar(this)
        val profileAdptar = profileAdaptar()
        val orderAdaptar = OrderAdaptar(arr2)
        homeAdptar.arr = arr2
        homeAdptar.list = arr1
        profileAdptar.arrayList = arr3
        binding.navMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    println("home")
                    val fragment = home_fragment.newInstance(homeAdptar)
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment)
                        .commit()
                }

                R.id.menu_add_medicine -> {
                    println("add")
                    val fragment = AddFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment)
                        .commit()
                }

                R.id.menu_order -> {
                    println("order")
                    val fragment = order_fragment.newInstance(orderAdaptar)
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment)
                        .commit()
                }

                R.id.menu_profile -> {
                    println("profile")
                    val fragment = ProfileFragment.newInstance(1, profileAdptar)
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame, fragment)
                        .commit()
                }
            }
            return@setOnItemSelectedListener true
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

            R.id.main_setting -> {
                Toast.makeText(baseContext, "${item.title}", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
    override fun addToCart(arr: SharedMedicine) {
        lifecycleScope.launch {
            val result = arr2.find { it.id == arr.id }
            if (result != null) {
                result.quantity += 1
                Toast.makeText(baseContext, "added", Toast.LENGTH_SHORT).show()
            }
        }
    }
}