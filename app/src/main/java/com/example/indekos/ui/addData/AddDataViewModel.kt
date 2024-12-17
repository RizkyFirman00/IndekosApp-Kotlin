package com.example.indekos.ui.addData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indekos.model.Indekos
import com.example.indekos.repository.IndekosRepository
import com.example.indekos.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDataViewModel @Inject constructor(
    private val indekosRepository: IndekosRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun insertIndekos(
        userId: Int,
        namaIndekos: String,
        harga: String,
        jumlah_bedroom: String? = null,
        jumlah_cupboard: String? = null,
        jumlah_kitchen: String? = null,
        latitde_indekos: Double,
        longitude_indekos: Double,
        alamat: String? = null,
        kota: String? = null,
        provinsi: String? = null,
        photoUrl: List<String>? = null,
        photoBannerUrl: String? = null
    ) {
        viewModelScope.launch {
            indekosRepository.insertIndekos(
                Indekos(
                    0,
                    userId,
                    namaIndekos,
                    harga,
                    jumlah_bedroom,
                    jumlah_cupboard,
                    jumlah_kitchen,
                    latitde_indekos,
                    longitude_indekos,
                    alamat,
                    kota,
                    provinsi,
                    photoUrl,
                    photoBannerUrl
                )
            )
        }
    }

    fun getUserbyId(userId: Int) = userRepository.getUserById(userId)
}