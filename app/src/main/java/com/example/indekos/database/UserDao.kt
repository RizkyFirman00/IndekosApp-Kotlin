package com.example.indekos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.indekos.model.Users

@Dao
interface UserDao {
    // Ga dipake
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<Users>

    // Fungsi register di RegisterActivity
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun registerUser(users: Users)

    // Fungsi check login di LoginActivity
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): Users?

    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserById(userId: Int): LiveData<Users>
}