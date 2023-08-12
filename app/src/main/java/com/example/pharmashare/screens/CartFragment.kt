package com.example.pharmashare.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.Cart
import com.example.pharmashare.firebase.objects.Order
import com.example.pharmashare.firebase.objects.SharedMedicine
import com.example.pharmashare.firebase.repos.CartRepository
import com.example.pharmashare.firebase.repos.OrderRepository
import com.example.pharmashare.firebase.repos.PharmacyRepository
import com.example.pharmashare.firebase.repos.UserRepository
import com.example.pharmashare.screens.adptars.CartAdapter
import java.text.DateFormat
import java.util.Date

class CartFragment : Fragment(), CartAdapter.ResultBack {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: CartAdapter
    lateinit var totalprice: TextView
    var Items: MutableList<Details> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val rootView = inflater.inflate(R.layout.fragment_order_fragment, container, false)
        totalprice = rootView.findViewById<TextView>(R.id.total_price)
        val btnDone = rootView.findViewById<TextView>(R.id.btn_done)

        recyclerView = rootView.findViewById(R.id.order_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val userId = UserRepository.getCurrentUserId()
        var pharmacyName = ""
        PharmacyRepository.getAllPharmaciesByOwnerId(userId) { pharmacyList ->
            pharmacyList.forEach {
                pharmacyName += it.name + "\n"
                println("$pharmacyName name ${it.name}")
            }
        }
        orderAdapter = CartAdapter(mutableListOf(), this)
        recyclerView.adapter = orderAdapter
        CartRepository.getCarts("NbFvwsXIUquhBsJLvRE") { cartItems ->
            orderAdapter = CartAdapter(cartItems as MutableList<Cart>, this)
            recyclerView.adapter = orderAdapter
            Log.d("orders", orderAdapter.getAllDate().toString())
            orderAdapter.getAllDate().forEach {
                Items.add(
                    Details(
                        medicine = it.medicine,
                        quantity = it.quantity,
                        price = it.priceTotal
                    )
                )
            }
        }
        val dateFormat = DateFormat.getDateInstance()
        btnDone.setOnClickListener {
            Log.d("name", "${pharmacyName.trim().length} name $userId")
            val order = Order(
                "Temp ${kotlin.random.Random.nextInt(100)}",
                //pharmacyName.trim(),
                "test",
                dateFormat.format(Date()).format("YYYY/MM/dd"),
                totalPrice = totalprice.text.toString().toDouble(),
                Items.toString(),
                "done"
            )
            OrderRepository.insertOrder(order) {
                Log.d("done", "$it")
            }
            CartRepository.removeCart(cart = Cart("", "Temp", "", 0, 0.0, 0.0)) {}
        }

        return rootView
    }

    override fun backPrice(totalPrice: Double) {
        totalprice.text = totalPrice.toString()
        Log.d("test is", "backPrice: $totalPrice ")
    }

    data class Details(
        val medicine: String,
        val quantity:Int    ,
        val price:Double
    ){
        override fun toString(): String {
            return "medicine name : $medicine price: $price quantity: $quantity"
        }
    }
}