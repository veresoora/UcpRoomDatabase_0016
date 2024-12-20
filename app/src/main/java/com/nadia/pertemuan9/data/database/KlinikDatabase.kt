package com.nadia.pertemuan9.data.database

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nadia.pertemuan9.data.dao.DokterDao
import com.nadia.pertemuan9.data.dao.JadwalDao
import com.nadia.pertemuan9.data.entity.Dokter
import com.nadia.pertemuan9.data.entity.Jadwal

@Database(entities = [Dokter::class, Jadwal::class], version = 1, exportSchema = false)
abstract class KlinikDatabase : RoomDatabase(){
    abstract fun dokterDao(): DokterDao
    abstract fun jadwalDao(): JadwalDao

    companion object{
        @Volatile
        private var Instances : KlinikDatabase? = null

        fun getDatabase(context: Context): KlinikDatabase{
            return (Instances ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    KlinikDatabase::class.java,
                    "KlinikDatabase"
                )
                    .build().also { Instances = it }
            })
        }
    }
}