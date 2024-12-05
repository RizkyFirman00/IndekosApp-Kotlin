package com.example.indekos.ui.map

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indekos.database.IndekosDao
import com.example.indekos.model.Indekos
import com.example.indekos.repository.IndekosRepository
import com.example.indekos.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val indekosRepository: IndekosRepository) : ViewModel() {

    private val _indekosList = MutableLiveData<List<Indekos>>()

    val indekosList: LiveData<List<Indekos>> = _indekosList

    fun getAllIndekos() {
        viewModelScope.launch {
            indekosRepository.getAllIndekos()
                .collect { indekos ->
                    _indekosList.value = indekos
                }
        }
    }
}