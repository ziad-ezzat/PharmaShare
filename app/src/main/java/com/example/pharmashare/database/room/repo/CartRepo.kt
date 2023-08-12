package com.example.pharmashare.database.room.repo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pharmashare.database.objects.Cart
import com.example.pharmashare.database.objects.Pharmacy

@Dao
interface CartRepo {

    @Insert
    fun insertByRoom(cart: Cart)
    @Update
    fun updateByRoom(cart: Cart)
    @Delete
    fun deleteItemByRoom(cart: Cart)
    @Delete
    fun deleteListByRoom(carts:List<Cart>)
    @Query("select * from cart_table where id = :id group BY pharmacyId")
    fun getAllDateByIdWithRoom(id:Int):List<Cart>
    @Query("select * from cart_table where pharmacyId = :pharmacyID group BY price")
    fun getAllDateByPharmacyIdWithRoom(pharmacyID:String):MutableList<Cart>

    // delete all data from cart_table
    @Query("DELETE FROM cart_table")
    fun deleteAll()

    // get all data from cart_table
    @Query("SELECT * FROM cart_table")
    fun getAll(): List<Cart>
}