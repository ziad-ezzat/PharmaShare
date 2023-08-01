package com.example.pharmashare.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pharmashare.database.entities.Medicine

@Dao
interface MedicineDao {
    @Insert
    suspend fun insert(medicine: Medicine)

    @Query("SELECT * FROM medicines WHERE name = :name")
    suspend fun getMedicineByName(name: String): Medicine

    @Query("SELECT * FROM medicines")
    suspend fun getAllMedicines(): List<Medicine>
}