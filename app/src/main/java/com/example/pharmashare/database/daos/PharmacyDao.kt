package com.example.pharmashare.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pharmashare.database.entities.Pharmacy

@Dao
interface PharmacyDao {
    @Insert
    suspend fun insert(pharmacy: Pharmacy)

    @Query("SELECT * FROM pharmacies WHERE owner_id = :userId")
    suspend fun getPharmaciesByUserId(userId: Int): List<Pharmacy>

    @Query("SELECT * FROM pharmacies WHERE id = :id")
    suspend fun getPharmacyById(id: Int): Pharmacy

    @Query("SELECT * FROM pharmacies WHERE name = :name")
    suspend fun getPharmacyByName(name: String): Pharmacy

    @Query("SELECT * FROM pharmacies WHERE address = :address")
    suspend fun getPharmacyByAddress(address: String): Pharmacy
}
