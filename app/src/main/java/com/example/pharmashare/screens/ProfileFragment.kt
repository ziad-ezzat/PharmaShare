package com.example.pharmashare.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.Order
import com.example.pharmashare.firebase.repos.OrderRepository
import com.example.pharmashare.firebase.repos.PharmacyRepository
import com.example.pharmashare.firebase.repos.UserRepository
import com.example.pharmashare.screens.adptars.ProfileAdapter

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val userId = UserRepository.getCurrentUserId()

        val pharmacyNameTV: TextView = rootView.findViewById(R.id.Profile_pharmacy)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.profile_rv)
        val profileName: TextView = rootView.findViewById(R.id.Profile_name)

        // Fetch user name and set it to the profileName TextView
        UserRepository.getCurrentUserName() { userName ->
            profileName.text = userName
        }

        // Fetch pharmacy names and set them to the pharmacyNameTV TextView
        PharmacyRepository.getAllPharmaciesByOwnerId(userId) { pharmacyList ->
            val pharmacyNames = pharmacyList.joinToString("\n") { it.name }
            pharmacyNameTV.text = pharmacyNames
            // Once pharmacy names are fetched, fetch the orders for these pharmacies
            fetchOrdersForPharmacies("se7a", recyclerView)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return rootView
    }

    private fun fetchOrdersForPharmacies(pharmacyNames: String, recyclerView: RecyclerView) {
        OrderRepository.getOrdersByName(pharmacyNames) { orders ->
            recyclerView.adapter = ProfileAdapter(orders)
        }
    }
}