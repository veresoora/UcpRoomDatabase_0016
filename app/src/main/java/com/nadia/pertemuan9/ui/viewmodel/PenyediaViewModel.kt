package com.nadia.pertemuan9.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nadia.pertemuan9.KlinikApp
import com.nadia.pertemuan9.ui.viewmodel.dokter.DokterViewModel
import com.nadia.pertemuan9.ui.viewmodel.dokter.HomeDokterViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.HomeJadwalViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.JadwalViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            DokterViewModel(
                klinikApp().containerApp.repositoryDokter
            )
        }

        initializer {
            HomeDokterViewModel(
                klinikApp().containerApp.repositoryDokter
            )
        }

        initializer {
            JadwalViewModel(
                klinikApp().containerApp.repositoryJadwal
            )
        }

        initializer {
            HomeJadwalViewModel(
                klinikApp().containerApp.repositoryJadwal
            )
        }
    }
}

fun CreationExtras.klinikApp(): KlinikApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KlinikApp)