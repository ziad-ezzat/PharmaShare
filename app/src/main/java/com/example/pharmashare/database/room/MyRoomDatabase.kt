package com.example.pharmashare.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pharmashare.database.objects.Cart
import com.example.pharmashare.database.room.repo.CartRepo

@Database(entities = [Cart::class], version = 2)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun cartDao(): CartRepo

    companion object {
        private const val DATABASE_NAME = "app_database"
        private val migration1To2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE cart_table RENAME TO cart_table_old")
                database.execSQL("""
                    CREATE TABLE cart_table (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        pharmacyId TEXT NOT NULL,
                        medicine TEXT NOT NULL,
                        quantity INTEGER NOT NULL,
                        price REAL NOT NULL,
                        priceTotal REAL NOT NULL,
                        availableQuantity INTEGER NOT NULL
                    )
                """.trimIndent())
                database.execSQL("INSERT INTO cart_table SELECT * FROM cart_table_old")
                database.execSQL("DROP TABLE cart_table_old")
            }
        }
        fun buildDatabase(context: Context): MyRoomDatabase {
            return Room.databaseBuilder(context, MyRoomDatabase::class.java, DATABASE_NAME)
                .addMigrations(migration1To2)
                .allowMainThreadQueries()
                .build()
        }
    }
}