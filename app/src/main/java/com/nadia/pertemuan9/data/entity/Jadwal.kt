package com.nadia.pertemuan9.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "jadwal")
data class Jadwal(
    @PrimaryKey(autoGenerate = true)
    val idjadwal: Int = 0,
    val dokter: String,
    val namapasien: String,
    val nomorhp: String,
    val tglkonsul: String,
    val status: String,
)
