package com.example.indekos.repository

import androidx.lifecycle.LiveData
import com.example.indekos.database.UserDao
import com.example.indekos.model.Users
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    fun registerUser(user: Users) {
        userDao.registerUser(user)
    }

    suspend fun getUserByUsername(username: String): Users? {
        return userDao.getUserByUsername(username)
    }

    fun getUserById(userId: Int): LiveData<Users> {
        return userDao.getUserById(userId)
    }
}

