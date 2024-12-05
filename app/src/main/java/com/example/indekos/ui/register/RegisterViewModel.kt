package com.example.indekos.ui.register

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.indekos.database.UserDao
import com.example.indekos.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    fun register(email: String, noTelp: String, username: String, password: String) {
        userRepository.registerUser(email, noTelp, username, password)
    }
}