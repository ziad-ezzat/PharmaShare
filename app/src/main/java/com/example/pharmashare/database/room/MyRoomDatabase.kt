package com.example.pharmashare.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pharmashare.database.objects.Cart
import com.example.pharmashare.database.room.repo.CartRepo

@Database(entities = [Cart::class], version = 1)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun cartDao(): CartRepo
}