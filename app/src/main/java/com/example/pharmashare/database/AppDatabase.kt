package com.example.pharmashare.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pharmashare.database.daos.*
import com.example.pharmashare.database.entities.*


@Database(entities = [User::class, Pharmacy::class, Medicine::class, Order::class, SharedMedicine::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun pharmacyDao(): PharmacyDao
    abstract fun medicineDao(): MedicineDao
    abstract fun orderDao(): OrderDao
    abstract fun shredMedicineDao(): SharedMedicineDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                ).allowMainThreadQueries().build().also { instance = it }
            }
        }

    }
}