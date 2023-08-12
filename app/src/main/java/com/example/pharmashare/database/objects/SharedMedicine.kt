package com.example.pharmashare.database.objects

data class SharedMedicine(
    val id: String = "",
    val userId: String = "",
    val pharmacyName: String = "",
    val medicineName: String = "",
    var quantity: Int = 1,
    val expiredDate: String = "",
    val price: Double = 0.0,
)
