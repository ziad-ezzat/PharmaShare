package com.example.pharmashare.database.firebase.repos

import androidx.room.Dao
import androidx.room.Insert
import com.example.pharmashare.database.objects.Cart
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object CartRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("cart")

    // insert a new cart into the database auto generated id
    fun insertCart(cart: Cart, callback: (Boolean) -> Unit) {
        val cartId = database.getReference("cart").push().key ?: ""
        val newCart = Cart(cartId.toInt(), cart.pharmacyId, cart.medicine, cart.quantity, cart.price, cart.priceTotal,cart.availableQuantity,cart.discountPercentage)

        database.getReference("cart").child(cartId).setValue(newCart)
            .addOnCompleteListener { createCartTask ->
                if (createCartTask.isSuccessful) {
                    callback(true) // Success
                } else {
                    callback(false) // Handle cart creation failure
                }
            }
    }

    // get all carts that the user make where PharmacyId = id
    fun getCarts(id: String, callback: (ArrayList<Cart>) -> Unit) {
        val carts = ArrayList<Cart>()
        usersRef.get().addOnSuccessListener { cartsSnapshot ->
            cartsSnapshot.children.forEach { cartSnapshot ->
                val cart = cartSnapshot.getValue(Cart::class.java)
                if (cart != null && cart.pharmacyId == id) {
                    carts.add(cart)
                }
            }
            callback(carts)
        }
    }

    // update cart quantity and priceTotal
    fun updateCart(cart: Cart, callback: (Boolean) -> Unit) {
        val cartId = cart.id
        val newCart = Cart(cartId, cart.pharmacyId, cart.medicine, cart.quantity, cart.price, cart.priceTotal,cart.availableQuantity,cart.discountPercentage)

        database.getReference("cart").child(cartId.toInt().toString()).setValue(newCart)
            .addOnCompleteListener { updateCartTask ->
                if (updateCartTask.isSuccessful) {
                    callback(true) // Success
                } else {
                    callback(false) // Handle cart update failure
                }
            }
    }

    // remove cart from database
    fun removeCart(cart: Cart, callback: (Boolean) -> Unit) {
        val cartId = cart.id
        database.getReference("cart").child(cartId.toInt().toString()).removeValue()
            .addOnCompleteListener { removeCartTask ->
                if (removeCartTask.isSuccessful) {
                    callback(true) // Success
                } else {
                    callback(false) // Handle cart removal failure
                }
            }
    }

}