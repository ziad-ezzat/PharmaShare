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
import androidx.room.Room
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.Cart
import com.example.pharmashare.firebase.objects.Order
import com.example.pharmashare.firebase.repos.OrderRepository
import com.example.pharmashare.firebase.repos.PharmacyRepository
import com.example.pharmashare.firebase.repos.UserRepository
import com.example.pharmashare.firebase.room.MyRoomDatabase
import com.example.pharmashare.screens.adptars.OrderAdapter
import java.text.DateFormat
import java.util.Date

class OrderFragment : Fragment(), OrderAdapter.ResultBack {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var totalPrice: TextView
    private lateinit var cartItems: MutableList<Cart>
    private var items: MutableList<Details> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val rootView = inflater.inflate(R.layout.fragment_order_fragment, container, false)
        totalPrice = rootView.findViewById<TextView>(R.id.total_price)
        val btnDone = rootView.findViewById<TextView>(R.id.btn_done)

        recyclerView = rootView.findViewById(R.id.order_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Database Dac
        val database =
            Room.databaseBuilder(container!!.context, MyRoomDatabase::class.java, "myDataBase")
                .allowMainThreadQueries().build()
        val dao = database.cartDao()

        val userId = UserRepository.getCurrentUserId()
        var pharmacyName = ""
        PharmacyRepository.getAllPharmaciesByOwnerId(userId) { pharmacyList ->
            pharmacyList.forEach {
                pharmacyName += it.name + "\n"
                println("$pharmacyName name ${it.name}")
            }
        }
        orderAdapter = OrderAdapter(mutableListOf(), this)
        recyclerView.adapter = orderAdapter
//        CartRepository.getCarts("NbFvwsXIUquhBsJLvRE") { cartItems ->
//            orderAdapter = OrderAdapter(cartItems as MutableList<Cart>, this)
//            recyclerView.adapter = orderAdapter
//            Log.d("orders", orderAdapter.getAllDate().toString())
//            orderAdapter.getAllDate().forEach {
//                items.add(
//                    Details(
//                        medicine = it.medicine,
//                        quantity = it.quantity,
//                        price = it.priceTotal
//                    )
//                )
//            }
//        }
        cartItems = dao.getAllDateByPharmacyIdWithRoom("NbFvwsXIUquhBsJLvRE")
        Log.d("size", "${cartItems.size} size1")
        orderAdapter = OrderAdapter(cartItems, this)
        recyclerView.adapter = orderAdapter
        Log.d("orders", orderAdapter.getAllDate().toString())
        orderAdapter.getAllDate().forEach {
            items.add(
                Details(
                    medicine = it.medicine,
                    quantity = it.quantity,
                    price = it.priceTotal
                )
            )
        }
        val dateFormat = DateFormat.getDateInstance()
        btnDone.setOnClickListener {
            Log.d("name", "${pharmacyName.trim().length} name $userId")
            val order = Order(
                "Temp ${kotlin.random.Random.nextInt(100)}",
                //pharmacyName.trim(),
                "test",
                dateFormat.format(Date()).format("YYYY/MM/dd"),
                totalPrice = totalPrice.text.toString().toDouble(),
                items.toString(),
                "done"
            )
            OrderRepository.insertOrder(order) {
                Log.d("done", "$it")
            }

//            CartRepository.removeCart(cart = Cart("", "Temp", "", 0, 0.0, 0.0,0)) {}
            dao.deleteListByRoom(cartItems)
            cartItems = dao.getAllDateByPharmacyIdWithRoom("NbFvwsXIUquhBsJLvRE")
            Log.d("size", "${cartItems.size} size")
            orderAdapter = OrderAdapter(cartItems,this)
            recyclerView.adapter = orderAdapter
        }

        return rootView
    }

    override fun backPrice(totalPrice: Double) {
        this.totalPrice.text = totalPrice.toString()
    }

    override fun checkItIsAvailable(available: Boolean) {
        // if we want to clear medicine
    }

    data class Details(
        val medicine: String,
        val quantity: Int,
        val price: Double
    ) {
        override fun toString(): String {
            return "medicine name : $medicine price: $price quantity: $quantity"
        }
    }
}