package com.example.pharmashare.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "orders", foreignKeys = [ForeignKey(entity = Pharmacy::class, parentColumns = ["id"], childColumns = ["pharmacy_id"], onDelete = ForeignKey.CASCADE)])
data class Order(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "pharmacy_id") val pharmacyId: Int,
    @ColumnInfo(name = "order_date") val orderDate: Date,
    @ColumnInfo(name = "total_price") val totalPrice: Double,
    @ColumnInfo(name = "order_details") val orderDetails: String,
    val status: String
)
