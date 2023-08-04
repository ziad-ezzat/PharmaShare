package com.example.pharmashare.firebase.objects

data class SharedMedicine(
    val id: String,
    val PharmacyId: String,
    val medicineId: String,
    val quantity: Int,
    val expiredDate: String,
    val price: Double
)
