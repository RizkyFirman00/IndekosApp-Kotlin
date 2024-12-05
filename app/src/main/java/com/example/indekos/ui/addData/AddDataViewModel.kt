package com.example.indekos.ui.addData

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.indekos.database.IndekosDao
import com.example.indekos.database.UserDao
import com.example.indekos.repository.IndekosRepository
import com.example.indekos.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddDataViewModel @Inject constructor(
    private val indekosRepository: IndekosRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {

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
        indekosRepository.insertIndekos(
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
    }

    fun getUserbyId(userId: Int) = userRepository.getUserById(userId)

}