package com.example.pharmashare.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.Cart
import com.example.pharmashare.firebase.objects.Pharmacy
import com.example.pharmashare.firebase.objects.SharedMedicine
import com.example.pharmashare.firebase.repos.CartRepository
import com.example.pharmashare.firebase.repos.PharmacyRepository
import com.example.pharmashare.firebase.repos.SharedMedicineRepository
import com.example.pharmashare.firebase.repos.UserRepository
import com.example.pharmashare.screens.adptars.HomeAdapter

class HomeFragment : Fragment(), HomeAdapter.HomeListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_fragment, container, false)

        recyclerView = rootView.findViewById(R.id.fragment_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Replace this with actual data fetching logic
        val userId = UserRepository.getCurrentUserId()
        SharedMedicineRepository.getAllSharedMedicines(userId) { sharedMedicines ->
            homeAdapter = HomeAdapter(sharedMedicines, this)
            recyclerView.adapter = homeAdapter
        }

        return rootView
    }
    override fun addToCart(sharedMedicine: SharedMedicine, quantity: Int, price: Double) {

        val cartItem = Cart("Temp","NbFvwsXIUquhBsJLvRE",sharedMedicine.medicineName, quantity, price , quantity*price)
        CartRepository.insertCart(cartItem) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }
}