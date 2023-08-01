package com.example.pharmashare.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "pharmacies", foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["owner_id"], onDelete = ForeignKey.CASCADE)])
data class Pharmacy(
    @PrimaryKey val id: Int,
    val name: String,
    val address: String,
    @ColumnInfo(name = "owner_id") val ownerId: Int
)
