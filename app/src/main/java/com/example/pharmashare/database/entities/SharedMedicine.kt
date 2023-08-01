package com.example.pharmashare.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "shared_medicines", foreignKeys = [ForeignKey(entity = Pharmacy::class, parentColumns = ["id"], childColumns = ["from_pharmacy_id"], onDelete = ForeignKey.CASCADE), ForeignKey(entity = Medicine::class, parentColumns = ["id"], childColumns = ["medicine_id"], onDelete = ForeignKey.CASCADE)])
data class SharedMedicine(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "from_pharmacy_id") val fromPharmacyId: Int,
    @ColumnInfo(name = "medicine_id") val medicineId: Int,
    val quantity: Int,
    @ColumnInfo(name = "expired_date") val expiredDate: Date,
    val price: Double
)
