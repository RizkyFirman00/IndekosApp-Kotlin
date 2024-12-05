package com.example.indekos.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.indekos.database.IndekosDao
import com.example.indekos.database.UserDao
import com.example.indekos.repository.IndekosRepository
import com.example.indekos.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val indekosRepository: IndekosRepository
) : ViewModel() {

    fun getByIndekosId(indekosId: Int) = indekosRepository.getIndekosById(indekosId)

    fun getUserById(userId: Int) = userRepository.getUserById(userId)
}