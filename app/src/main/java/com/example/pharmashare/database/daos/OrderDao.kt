package com.example.pharmashare.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pharmashare.database.entities.Order

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: Order)

    @Query("SELECT * FROM orders WHERE pharmacy_id = (SELECT id FROM pharmacies WHERE name = :pharmacyName)")
    suspend fun getOrdersByPharmacyName(pharmacyName: String): List<Order>

    @Query("SELECT * FROM orders WHERE pharmacy_id = :pharmacyId")
    suspend fun getOrdersByPharmacyId(pharmacyId: Int): List<Order>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: Int): Order

    @Query("SELECT * FROM orders WHERE status = :status")
    suspend fun getOrdersByStatus(status: String): List<Order>

    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<Order>

    @Query("SELECT * FROM orders WHERE order_date = :orderDate")
    suspend fun getOrdersByOrderDate(orderDate: String): List<Order>
}