package com.example.pharmashare.firebase.room.repo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pharmashare.firebase.objects.Cart
import com.example.pharmashare.firebase.objects.Pharmacy

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


}