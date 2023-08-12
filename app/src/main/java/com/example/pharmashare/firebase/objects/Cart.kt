package com.example.pharmashare.firebase.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cart_table")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pharmacyId: String = "",
    val medicine: String = "",
    var quantity: Int = 0,
    val price: Double = 0.0,
    var priceTotal: Double = 0.0,
    var availableQuantity:Int = 0,
    var discountPercentage:Double
)
