package com.example.pharmashare.screens

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pharmashare.R
import com.example.pharmashare.database.AppDatabase
import com.example.pharmashare.database.entities.Medicine
import com.example.pharmashare.database.entities.Order
import com.example.pharmashare.database.entities.SharedMedicine
import com.example.pharmashare.databinding.ActivityHomeBinding
import com.example.pharmashare.screens.adptars.HomeAdaptar
import com.example.pharmashare.screens.adptars.profileAdaptar
import kotlinx.coroutines.launch
import java.util.Date

class Home : AppCompatActivity(), HomeAdaptar.HomeLinsener {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    val arr1 = arrayListOf<Medicine>(
        Medicine(1, "test01"),
        Medicine(2, "test02"),
        Medicine(3, "test03"),
        Medicine(4, "test04"),
        Medicine(5, "test05")
    )
    val arr2 = arrayListOf<SharedMedicine>(
        SharedMedicine(1, 1, 1, 12, Date(), 15.0),
        SharedMedicine(1, 1, 2, 30, Date(), 4.0),
        SharedMedicine(1, 1, 3, 8, Date(), 32.0)
    )
    val arr3 = arrayListOf<Order>(
        Order(1, 1, Date(), 306.0, "ok01", "done"),
        Order(2, 2, Date(), 256.0, "ok02", "failed"),
        Order(3, 3, Date(), 466.0, "ok03", "on-process"),
    )
    val appDateBase: AppDatabase by lazy {
        AppDatabase.getInstance(baseContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val homeAdptar = HomeAdaptar(this)
        val orderAdptar = profileAdaptar()
        homeAdptar.arr = arr2
        homeAdptar.list = arr1
        orderAdptar.arrayList = arr3
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
//           appDateBase.medicineDao().insert(arr1[0])
//           appDateBase.shredMedicineDao().insert(arr)
//           println(appDateBase.shredMedicineDao().getAllSharedMedicines().size)
        }
    }
}