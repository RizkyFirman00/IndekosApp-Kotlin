package com.example.indekos.repository

import androidx.lifecycle.LiveData
import com.example.indekos.database.IndekosDao
import com.example.indekos.model.Indekos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndekosRepository @Inject constructor(private val indekosDao: IndekosDao) {

    fun insertIndekos(indekos: Indekos) {
        indekosDao.insertIndekos(indekos)
    }

    fun getAllIndekos(): Flow<List<Indekos>> = indekosDao.getAllIndekos()

    fun getIndekosById(indekosId: Int): LiveData<Indekos> = indekosDao.getIndekosById(indekosId)

    fun searchIndekos(query: String?): Flow<List<Indekos>> = indekosDao.searchIndekos(query)

    fun updateIndekos(indekos: Indekos) {
        indekosDao.updateIndekos(indekos)
    }

    fun deleteIndekos(indekos: Indekos) {
        indekosDao.deleteIndekos(indekos)
    }
}
