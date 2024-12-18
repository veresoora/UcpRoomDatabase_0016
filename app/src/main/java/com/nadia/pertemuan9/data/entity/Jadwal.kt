package com.nadia.pertemuan9.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "jadwal",
    foreignKeys = [
        ForeignKey(
            entity = Dokter::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("id"),
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Jadwal(
    @PrimaryKey
    val idjadwal: String,
    val id: String,
    val namapasien: String,
    val nomorhp: String,
    val tglkonsul: String,
    val status: String,
)
