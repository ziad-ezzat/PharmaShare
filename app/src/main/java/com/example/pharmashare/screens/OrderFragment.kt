package com.example.pharmashare.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.Cart
import com.example.pharmashare.firebase.objects.Order
import com.example.pharmashare.firebase.repos.CartRepository
import com.example.pharmashare.firebase.repos.OrderRepository
import com.example.pharmashare.screens.adptars.OrderAdapter

class OrderFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_order_fragment, container, false)
        val totalprice = rootView.findViewById<TextView>(R.id.total_price)
        val btnDone = rootView.findViewById<TextView>(R.id.btn_done)

        recyclerView = rootView.findViewById(R.id.order_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        orderAdapter = OrderAdapter(mutableListOf())
        recyclerView.adapter = orderAdapter

        // Observe the LiveData for total price updates
        orderAdapter.getTotalLiveData().observe(viewLifecycleOwner) { totalPrice ->
            totalprice.text = totalPrice.toString()
        }


        CartRepository.getCarts("NbFvwsXIUquhBsJLvRE") { cartItems ->
            orderAdapter = OrderAdapter(cartItems as MutableList<Cart>)
            recyclerView.adapter = orderAdapter
        }

        btnDone.setOnClickListener {
            val order = Order(
                "Temp",
                "se7a",
                "7/8/2024",
                100.0,
                "Temp",
                "Temp")
            OrderRepository.insertOrder(order) {}
            CartRepository.removeCart(cart = Cart("", "Temp", "", 0,0.0,0.0)) {}
        }

        return rootView
    }
}