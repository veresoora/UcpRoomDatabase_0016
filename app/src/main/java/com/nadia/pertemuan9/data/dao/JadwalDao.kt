package com.nadia.pertemuan9.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nadia.pertemuan9.data.entity.Jadwal
import kotlinx.coroutines.flow.Flow

@Dao
interface JadwalDao {
    @Insert
    suspend fun insertJadwal(jadwal: Jadwal)

    @Query("SELECT * FROM jadwal ORDER BY namapasien ASC")
    fun getAllJadwal(): Flow<List<Jadwal>>

    @Query("SELECT * FROM jadwal WHERE idjadwal = :idjadwal")
    fun getJadwal(idjadwal : Int): Flow<Jadwal>

    @Delete
    suspend fun deleteJadwal (jadwal: Jadwal)

    @Update
    suspend fun updateJadwa (jadwal: Jadwal)
}