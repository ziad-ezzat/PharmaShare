package com.example.pharmashare.database.objects

data class Order(
    val id: String = "",
    val pharmacyName: String = "",
    val orderDate: String = "",
    val totalPrice: Double = 0.0,
    val orderDetails: String = "",
    val status: String = ""
)
