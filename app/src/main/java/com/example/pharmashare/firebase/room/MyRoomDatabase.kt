package com.example.pharmashare.firebase.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pharmashare.firebase.objects.Cart
import com.example.pharmashare.firebase.room.repo.CartRepo

@Database(entities = [Cart::class], version = 1)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun cartDao(): CartRepo
}