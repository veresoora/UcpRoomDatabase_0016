package com.nadia.pertemuan9.repository

import com.nadia.pertemuan9.data.dao.JadwalDao
import com.nadia.pertemuan9.data.entity.Dokter
import com.nadia.pertemuan9.data.entity.Jadwal
import kotlinx.coroutines.flow.Flow

class LocalRepositoryJadwal (
    private val jadwalDao: JadwalDao
) :RepositoryJadwal{
    override suspend fun insertJadwal(jadwal: Jadwal) {
        jadwalDao.insertJadwal(jadwal)
    }

    override suspend fun updateJadwal(jadwal: Jadwal) {
        jadwalDao.updateJadwa(jadwal)
    }

    override suspend fun deleteJadwal(jadwal: Jadwal) {
        jadwalDao.deleteJadwal(jadwal)
    }

    override fun getAllJadwal(): Flow<List<Jadwal>> {
        return jadwalDao.getAllJadwal()
    }

    override fun getJadwal(idjadwal: Int): Flow<Jadwal> {
        return jadwalDao.getJadwal(idjadwal)
    }

    override fun getNamaDokter(): Flow<List<Dokter>> {
        return jadwalDao.getNamaDokter()
    }
}