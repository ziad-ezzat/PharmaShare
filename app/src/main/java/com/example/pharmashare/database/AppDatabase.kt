package com.example.pharmashare.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pharmashare.database.daos.*
import com.example.pharmashare.database.entities.*


@Database(entities = [User::class, Pharmacy::class, Medicine::class, Order::class, SharedMedicine::class], version = 2)
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
                ).addMigrations(MIGRATION_1_2).allowMainThreadQueries().build().also { instance = it }
            }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new column to the existing table
                database.execSQL("ALTER TABLE users ADD COLUMN is_logged_in INTEGER NOT NULL DEFAULT 0")
            }
        }

    }
}