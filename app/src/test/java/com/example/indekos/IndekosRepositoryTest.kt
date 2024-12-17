package com.example.indekos

import android.app.Application
import com.example.indekos.database.IndekosDao
import com.example.indekos.database.MyAppRoomDatabase
import com.example.indekos.model.Indekos
import com.example.indekos.repository.IndekosRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class IndekosRepositoryTest {

    private lateinit var repository: IndekosRepository
    private lateinit var mockIndekosDao: IndekosDao

    @Before
    fun setup() {
        mockIndekosDao = mockk()
        repository = IndekosRepository(mockIndekosDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getAllIndekos_returnsListOfIndekos`() = runTest {
        val indekosList = listOf(
            Indekos(
                indekosId = 1,
                userId = 101,
                name_indekos = "Indekos Asri",
                harga = "1500000",
                jumlah_bedroom = "2",
                jumlah_cupboard = "1",
                jumlah_kitchen = "1",
                latitude_indekos = -6.200000,
                longitude_indekos = 106.816666,
                alamat = "Jalan Mawar No. 5",
                kota = "Jakarta",
                provinsi = "DKI Jakarta",
                photoUrl = listOf(
                    "null",
                    "null"
                ),
                photoBannerUrl = "null"
            ),
            Indekos(
                indekosId = 1,
                userId = 101,
                name_indekos = "Indekos Asri",
                harga = "1500000",
                jumlah_bedroom = "2",
                jumlah_cupboard = "1",
                jumlah_kitchen = "1",
                latitude_indekos = -6.200000,
                longitude_indekos = 106.816666,
                alamat = "Jalan Mawar No. 5",
                kota = "Jakarta",
                provinsi = "DKI Jakarta",
                photoUrl = listOf(
                    "null",
                    "null"
                ),
                photoBannerUrl = "null"
            ),
        )
        every { repository.getAllIndekos() } returns flowOf(indekosList)

        val result = repository.getAllIndekos()
        result.collect { list ->
            assertEquals(2, list.size)
            assertEquals("Indekos Asri", list[0].name_indekos)
        }
        verify { repository.getAllIndekos() }
    }

    @Test
    fun `insertIndekos calls DAO insertIndekos`() {
        val indekos = Indekos(
            indekosId = 1,
            userId = 101,
            name_indekos = "Indekos Asri",
            harga = "1500000",
            jumlah_bedroom = "2",
            jumlah_cupboard = "1",
            jumlah_kitchen = "1",
            latitude_indekos = -6.200000,
            longitude_indekos = 106.816666,
            alamat = "Jalan Mawar No. 5",
            kota = "Jakarta",
            provinsi = "DKI Jakarta",
            photoUrl = listOf(
                "null",
                "null"
            ),
            photoBannerUrl = "null"
        )
        every { mockIndekosDao.insertIndekos(any()) } just Runs

//        repository.insertIndekos(
//            userId = indekos.userId,
//            namaIndekos = indekos.name_indekos!!,
//            harga = indekos.harga!!,
//            jumlah_bedroom = indekos.jumlah_bedroom,
//            jumlah_cupboard = indekos.jumlah_cupboard,
//            jumlah_kitchen = indekos.jumlah_kitchen,
//            latitde_indekos = indekos.latitude_indekos!!,
//            longitude_indekos = indekos.longitude_indekos!!,
//            alamat = indekos.alamat,
//            kota = indekos.kota,
//            provinsi = indekos.provinsi,
//            photoUrl = indekos.photoUrl,
//            photoBannerUrl = indekos.photoBannerUrl
//        )

        verify { mockIndekosDao.insertIndekos(any()) }
    }

    @Test
    fun `deleteIndekos calls DAO deleteIndekos`() = runTest {
        val indekos = Indekos(
            indekosId = 1,
            userId = 101,
            name_indekos = "Indekos Asri",
            harga = "1500000",
            jumlah_bedroom = "2",
            jumlah_cupboard = "1",
            jumlah_kitchen = "1",
            latitude_indekos = -6.200000,
            longitude_indekos = 106.816666,
            alamat = "Jalan Mawar No. 5",
            kota = "Jakarta",
            provinsi = "DKI Jakarta",
            photoUrl = listOf(
                "null",
                "null"
            ),
            photoBannerUrl = "null"
        )

        coEvery { mockIndekosDao.deleteIndekos(any()) } just Runs

        repository.deleteIndekos(indekos)

        coVerify { mockIndekosDao.deleteIndekos(indekos) }
    }

}