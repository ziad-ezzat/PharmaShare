package com.example.pharmashare.app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.pharmashare.R
import com.example.pharmashare.database.objects.Cart
import com.example.pharmashare.database.objects.SharedMedicine
import com.example.pharmashare.database.firebase.repos.SharedMedicineRepository
import com.example.pharmashare.database.firebase.repos.UserRepository
import com.example.pharmashare.database.room.MyRoomDatabase
import com.example.pharmashare.database.room.repo.CartRepo
import com.example.pharmashare.app.adptars.HomeAdapter
import com.example.pharmashare.database.firebase.repos.PharmacyRepository

class HomeFragment : Fragment(), HomeAdapter.HomeListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    companion object {
        private lateinit var database: MyRoomDatabase
    }
    private lateinit var dao: CartRepo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_fragment, container, false)

        recyclerView = rootView.findViewById(R.id.fragment_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //declare of DB
        database = MyRoomDatabase.buildDatabase(context!!)
        dao = database.cartDao()
        val userId = UserRepository.getCurrentUserId()
        SharedMedicineRepository.getAllSharedMedicinesWithQuantity(userId) { sharedMedicines ->
            homeAdapter = HomeAdapter(sharedMedicines, this)
            recyclerView.adapter = homeAdapter
        }

        return rootView
    }
    override fun addToCart(sharedMedicine: SharedMedicine, selectedQuantity: Int, price: Double,pharmacyId: String): Boolean {
        val cart = Cart(
            pharmacyId = pharmacyId,
            medicine = sharedMedicine.medicineName,
            quantity = selectedQuantity,
            price = price,
            priceTotal = selectedQuantity * price,
            availableQuantity = sharedMedicine.quantity,
            sharedMedicineId = sharedMedicine.id
        )
        // check if medicine exist in cart if yes show message else add it
        val cartItem = dao.checkIfMedicineExist(sharedMedicineId = sharedMedicine.id)
            if (cartItem == null) {
                dao.insertByRoom(cart)
                return true
            } else {
                Toast.makeText(context, "This medicine already exist in cart", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
        return true
    }
}