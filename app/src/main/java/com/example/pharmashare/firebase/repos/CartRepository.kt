package com.example.pharmashare.firebase.repos

import com.example.pharmashare.firebase.objects.Cart
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object CartRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("cart")

    // add to cart
    fun insertCart(medicine: Cart) {
        usersRef.child(medicine.medicine.id).setValue(medicine)
    }

    // get cart
    fun getCart(callback: (ArrayList<Cart>) -> Unit) {
        val cart = ArrayList<Cart>()
        usersRef.get().addOnSuccessListener { cartSnapshot ->
            cartSnapshot.children.forEach { cartSnapshot ->
                val medicine = cartSnapshot.getValue(Cart::class.java)
                if (medicine != null) {
                    cart.add(medicine)
                }
            }
            callback(cart)
        }
    }

    // remove from cart
    fun removeCart(medicine: Cart) {
        usersRef.child(medicine.medicine.id).removeValue()
    }

}