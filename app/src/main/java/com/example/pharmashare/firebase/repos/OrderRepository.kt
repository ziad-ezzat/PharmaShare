package com.example.pharmashare.firebase.repos

import com.example.pharmashare.firebase.objects.Order
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object OrderRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("orders")


    // fun insert order that pharmacy make
    fun insertOrder(order: Order) {
        usersRef.child(order.id).setValue(order)
    }

    // fun get all orders that the pharmacy make where pharmacyId = id
    fun getOrders(id: String, callback: (ArrayList<Order>) -> Unit) {
        val orders = ArrayList<Order>()
        usersRef.get().addOnSuccessListener { ordersSnapshot ->
            ordersSnapshot.children.forEach { orderSnapshot ->
                val order = orderSnapshot.getValue(Order::class.java)
                if (order != null && order.pharmacyId == id) {
                    orders.add(order)
                }
            }
            callback(orders)
        }
    }

}