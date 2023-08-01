package com.example.pharmashare.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pharmashare.database.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Query("SELECT * FROM users WHERE phone_number = :phoneNumber AND password = :password")
    suspend fun checkLoginCredentials(phoneNumber: String, password: String): User
}