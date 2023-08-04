package com.example.pharmashare.firebase.objects

data class Order(
    val id: String,
    val pharmacyId: String,
    val orderDate: String,
    val totalPrice: Double,
    val orderDetails: String,
    val status: String
)
