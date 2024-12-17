package com.example.indekos.ui.history

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val indekosRepository: IndekosRepository) :
    ViewModel() {

    private val _indekosList = MutableStateFlow<List<Indekos>>(emptyList())
    val indekosList: StateFlow<List<Indekos>> = _indekosList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getListIndekosByUserId(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            indekosRepository.getIndekosById(userId).observeForever { indekos ->
                _indekosList.value = listOf(indekos)
            }
            _isLoading.value = false
        }
    }
}