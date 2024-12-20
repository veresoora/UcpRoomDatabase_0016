package com.nadia.pertemuan9.repository

import com.nadia.pertemuan9.data.entity.Dokter
import kotlinx.coroutines.flow.Flow

interface RepositoryDokter {
    suspend fun insertDokter(dokter: Dokter)

    fun getAllDokter () : Flow<List<Dokter>>
}