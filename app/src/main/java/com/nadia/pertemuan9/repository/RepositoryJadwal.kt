package com.nadia.pertemuan9.repository

import com.nadia.pertemuan9.data.entity.Jadwal
import kotlinx.coroutines.flow.Flow

interface RepositoryJadwal {
    suspend fun insertJadwal(jadwal: Jadwal)

    fun getAllJadwal () : Flow<List<Jadwal>>

    fun getJadwal(idjadwal: Int) : Flow<Jadwal>

    suspend fun updateJadwal(jadwal: Jadwal)

    suspend fun deleteJadwal(jadwal: Jadwal)
}