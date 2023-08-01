package com.example.pharmashare.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pharmashare.database.entities.SharedMedicine

@Dao
interface SharedMedicineDao {
    @Insert
    suspend fun insert(sharedMedicine: SharedMedicine)

    @Query("SELECT * FROM shared_medicines WHERE from_pharmacy_id = :pharmacyId AND medicine_id = :medicineId")
    suspend fun getSharedMedicineByPharmacyAndMedicineId(pharmacyId: Int, medicineId: Int): SharedMedicine

    @Query("SELECT * FROM shared_medicines WHERE from_pharmacy_id = :pharmacyId")
    suspend fun getSharedMedicineByPharmacyId(pharmacyId: Int): List<SharedMedicine>

    @Query("SELECT * FROM shared_medicines WHERE medicine_id = :medicineId")
    suspend fun getSharedMedicineByMedicineId(medicineId: Int): List<SharedMedicine>

    @Query("SELECT * FROM shared_medicines WHERE expired_date = :expiredDate")
    suspend fun getSharedMedicineByExpiredDate(expiredDate: String): List<SharedMedicine>

    @Query("SELECT * FROM shared_medicines WHERE price = :price")
    suspend fun getSharedMedicineByPrice(price: Double): List<SharedMedicine>

    @Query("SELECT * FROM shared_medicines WHERE quantity = :quantity")
    suspend fun getSharedMedicineByQuantity(quantity: Int): List<SharedMedicine>

    @Query("SELECT * FROM shared_medicines")
    suspend fun getAllSharedMedicines(): List<SharedMedicine>

    @Query("SELECT * FROM shared_medicines WHERE medicine_id = (SELECT id FROM medicines WHERE name = :medicineName)")
    suspend fun getSharedMedicineByMedicineName(medicineName: String): List<SharedMedicine>
}