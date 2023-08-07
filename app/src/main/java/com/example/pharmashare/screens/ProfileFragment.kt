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
        var pharmacyName = ""
        PharmacyRepository.getPharmacyByName(userId) { pharmacy ->
            if (pharmacy != null) {
                pharmacyName = pharmacy.name
            }
        }
        var prfileItems = ArrayList<Order>()
        OrderRepository.getOrdersByName(pharmacyName) { orders ->
            if (orders != null) {
                prfileItems = orders
            }
        }

        val recyclerView: RecyclerView = rootView.findViewById(R.id.profile_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ProfileAdapter(prfileItems)

        return rootView
    }
}