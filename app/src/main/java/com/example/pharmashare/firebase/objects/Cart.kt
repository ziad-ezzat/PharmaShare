package com.example.pharmashare.firebase.objects

data class Cart(
    val id: String = "",
    val pharmacyId: String = "",
    val medicine: String = "",
    var quantity: Int = 0,
    val price: Double = 0.0,
    var priceTotal: Double = 0.0
)
