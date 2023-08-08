package com.example.pharmashare.screens

import android.os.Bundle
import android.util.Log
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
        /*
        *
        *  val userId = UserRepository.getCurrentUserId()
        PharmacyRepository.getAllPharmaciesByOwnerId(userId) { pharmacies ->
            val pharmacyNames = pharmacies.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pharmacyNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            pharmacySpinner.adapter = adapter
        }
        * */
        val userId = UserRepository.getCurrentUserId()
        var pharmacyName = ""
        val pharmacyNameTV:TextView = rootView.findViewById(R.id.Profile_pharmacy)
        PharmacyRepository.getAllPharmaciesByOwnerId(userId) { pharmacyList ->
            pharmacyList.forEach {
                pharmacyName += it.name +"\n"
            }
            pharmacyNameTV.text = pharmacyName
        }
        var prfileItems = ArrayList<Order>()
        OrderRepository.getOrdersByName(pharmacyName) { orders ->
            prfileItems = orders
            println(orders.size)
        }

        val recyclerView: RecyclerView = rootView.findViewById(R.id.profile_rv)
        val profileName:TextView = rootView.findViewById(R.id.Profile_name)
        profileName.text = userId

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ProfileAdapter(prfileItems)

        return rootView
    }
}