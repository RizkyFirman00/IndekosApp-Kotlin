package com.example.indekos.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.indekos.database.IndekosDao
import com.example.indekos.database.UserDao
import com.example.indekos.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    suspend fun checkCredentials(username: String, password: String): Boolean {
        val user = userRepository.getUserByUsername(username)
        return user != null && user.password == password
    }

    suspend fun getUserId(username: String): String {
        val user = userRepository.getUserByUsername(username)
        return user?.userId.toString()
    }
}