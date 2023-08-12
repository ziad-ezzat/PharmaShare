package com.example.pharmashare.database.firebase.repos

import com.example.pharmashare.database.objects.Order
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object OrderRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("orders")


    // insert a new order into the database auto generated id
    fun insertOrder(order: Order, callback: (Boolean) -> Unit) {
        val orderId = database.getReference("orders").push().key ?: ""
        val newOrder = Order(orderId, order.pharmacyName, order.orderDate, order.totalPrice, order.orderDetails, order.status)

        database.getReference("orders").child(orderId).setValue(newOrder)
            .addOnCompleteListener { createOrderTask ->
                if (createOrderTask.isSuccessful) {
                    callback(true) // Success
                } else {
                    callback(false) // Handle order creation failure
                }
            }
    }

    // fun get all orders that the pharmacy make where pharmacyId = id
    fun getOrders(id: String, callback: (ArrayList<Order>) -> Unit) {
        val orders = ArrayList<Order>()
        usersRef.get().addOnSuccessListener { ordersSnapshot ->
            ordersSnapshot.children.forEach { orderSnapshot ->
                val order = orderSnapshot.getValue(Order::class.java)
                if (order != null && order.pharmacyName == id) {
                    orders.add(order)
                }
            }
            callback(orders)
        }
    }

    // return all orders that the pharmacy make where pharmacyName = name
    fun getOrdersByName(name: String, callback: (ArrayList<Order>) -> Unit) {
        val orders = ArrayList<Order>()
        usersRef.get().addOnSuccessListener { ordersSnapshot ->
            ordersSnapshot.children.forEach { orderSnapshot ->
                val order = orderSnapshot.getValue(Order::class.java)
                if (order != null && order.pharmacyName == name) {
                    orders.add(order)
                }
            }
            callback(orders)
        }
    }

    // remove order from database
    fun removeOrder(order: Order, callback: (Boolean) -> Unit) {
        val orderId = order.id
        database.getReference("orders").child(orderId).removeValue()
            .addOnCompleteListener { removeOrderTask ->
                if (removeOrderTask.isSuccessful) {
                    callback(true) // Success
                } else {
                    callback(false) // Handle order remove failure
                }
            }
    }

}