package com.example.pharmashare.app.fragments

import android.app.AlertDialog
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
import com.example.pharmashare.app.adptars.CartAdapter
import com.example.pharmashare.database.firebase.repos.OrderRepository
import com.example.pharmashare.database.firebase.repos.UserRepository
import com.example.pharmashare.database.objects.Cart
import com.example.pharmashare.database.objects.Order
import com.example.pharmashare.database.room.MyRoomDatabase
import java.text.DateFormat
import java.util.Date

class CartFragment : Fragment(), CartAdapter.ResultBack {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: CartAdapter
    private lateinit var totalPrice: TextView
    private lateinit var cartItems: MutableList<Cart>
    private var items: MutableList<Details> = mutableListOf()

    // Database Dac
    val database by lazy {
        MyRoomDatabase.buildDatabase(requireContext())
    }
    val dao by lazy {
        database.cartDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val rootView = inflater.inflate(R.layout.fragment_order_fragment, container, false)
        totalPrice = rootView.findViewById<TextView>(R.id.total_price)
        val btnDone = rootView.findViewById<TextView>(R.id.btn_done)

        recyclerView = rootView.findViewById(R.id.order_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val userId = UserRepository.getCurrentUserId()

        orderAdapter = CartAdapter(mutableListOf(), this)
        recyclerView.adapter = orderAdapter

        cartItems = dao.getAll() as MutableList<Cart>
        orderAdapter = CartAdapter(cartItems, this)
        recyclerView.adapter = orderAdapter
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
            if (cartItems.isEmpty()) {
                return@setOnClickListener
            }

            // Build the confirmation dialog
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Confirm Order")
                .setMessage("Are you sure you want to place this order?")
                .setPositiveButton("Yes") { _, _ ->
                    // User confirmed, proceed to place the order
                    val order = Order(
                        id = "temp",
                        currentUserId = userId,
                        orderDate = dateFormat.format(Date()).format("YYYY/MM/dd"),
                        totalPrice = totalPrice.text.toString().toDouble(),
                        orderDetails = items.toString(),
                        status = "done"
                    )
                    OrderRepository.insertOrder(order) {
                        Log.d("done", "$it")
                    }
                    dao.deleteAll()
                    cartItems = dao.getAll() as MutableList<Cart>
                    orderAdapter = CartAdapter(cartItems, this)
                    recyclerView.adapter = orderAdapter
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // User canceled, do nothing
                    dialog.dismiss()
                }

            // Show the confirmation dialog
            alertDialogBuilder.create().show()
        }

        return rootView
    }

    override fun backPrice(totalPrice: Double) {
        this.totalPrice.text = totalPrice.toString()
    }

    override fun checkItIsAvailable(available: Boolean) {
        // if we want to clear medicine
    }

    override fun deleteItemFromCart(cartItem: Cart) {
        dao.deleteItemByRoom(cartItem)
        orderAdapter = CartAdapter(dao.getAll() as MutableList<Cart>,this)
        recyclerView.adapter = orderAdapter
    }

    data class Details(
        val medicine: String,
        val quantity: Int,
        val price: Double
    ) {
        override fun toString(): String {
            return "\nmedicine name : $medicine \n price: $price\n quantity: $quantity"
        }
    }
}