package com.nadia.pertemuan9.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nadia.pertemuan9.KlinikApp
import com.nadia.pertemuan9.ui.viewmodel.dokter.DokterViewModel
import com.nadia.pertemuan9.ui.viewmodel.dokter.HomeDokterViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.DetailJadwalViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.HomeJadwalViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.JadwalViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.UpdateJadwalViewModel

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

        initializer {
            DetailJadwalViewModel(
                createSavedStateHandle(),
                klinikApp().containerApp.repositoryJadwal
            )
        }

        initializer {
            UpdateJadwalViewModel(
                createSavedStateHandle(),
                klinikApp().containerApp.repositoryJadwal
            )
        }
    }
}

fun CreationExtras.klinikApp(): KlinikApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KlinikApp)