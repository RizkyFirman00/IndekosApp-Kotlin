package com.example.indekos.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.indekos.database.IndekosDao
import com.example.indekos.database.MyAppRoomDatabase
import com.example.indekos.database.UserDao
import com.example.indekos.model.Indekos
import com.example.indekos.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(private val _UserDao: UserDao) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun registerUser(email: String, noTelp: String, username: String, password: String) {
        executorService.execute {
            _UserDao.registerUser(
                Users(
                    email = email,
                    noTelp = noTelp,
                    username = username,
                    password = password
                )
            )
        }
    }

    suspend fun getUserByUsername(username: String): Users? {
        return _UserDao.getUserByUsername(username)
    }

    fun getUserById(userId: Int): LiveData<Users> {
        return _UserDao.getUserById(userId)
    }
}
