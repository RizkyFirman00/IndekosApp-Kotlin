package com.example.indekos.ui.detailHistory

import android.app.Application
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
class DetailHistoryViewModel @Inject constructor(private val indekosRepository: IndekosRepository) :
    ViewModel() {

    fun updateIndekos(
        indekosId: Int,
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
            indekosRepository.updateIndekos(
                Indekos(
                    indekosId,
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

    fun getIndekosById(indekosId: Int) = indekosRepository.getIndekosById(indekosId)

    fun deleteIndekos(indekos: Indekos) {
        viewModelScope.launch {
            indekosRepository.deleteIndekos(indekos)
        }
    }
}