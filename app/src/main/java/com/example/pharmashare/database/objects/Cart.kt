package com.example.pharmashare.database.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.annotations.NotNull

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
    val sharedMedicineId: String = ""
)
