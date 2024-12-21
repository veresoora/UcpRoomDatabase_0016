package com.nadia.pertemuan9.ui.viewmodel.jadwal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadia.pertemuan9.data.entity.Jadwal
import com.nadia.pertemuan9.repository.RepositoryJadwal
import com.nadia.pertemuan9.ui.navigation.AlamatEditJadwal
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateJadwalViewModel (
    savedStateHandle: SavedStateHandle,
    private val repositoryJadwal: RepositoryJadwal
) : ViewModel() {
    var updateUIState by mutableStateOf(JadwalUIState())
        private set

    private val _idjadwal: Int = checkNotNull(savedStateHandle[AlamatEditJadwal.IDJWL])

    init {
        viewModelScope.launch {
            updateUIState = repositoryJadwal.getJadwal(_idjadwal)
                .filterNotNull()
                .first()
                .toUIStateJadwal()
        }
    }

    fun updateState(jadwalEvent: JadwalEvent){
        updateUIState = updateUIState.copy(
            jadwalEvent = jadwalEvent
        )
    }

    fun validateFields(): Boolean {
        val event = updateUIState.jadwalEvent
        val errorState = FormJadwalErrorState(
            dokter = if (event.dokter.isNotEmpty()) null else "Nama Dokter tidak boleh kosong",
            namapasien = if (event.namapasien.isNotEmpty()) null else "Nama Pasien tidak boleh kosong",
            nomorhp = if (event.nomorhp.isNotEmpty()) null else "Nomor Hp Pasien tidak boleh kosong",
            tglkonsul = if (event.tglkonsul.isNotEmpty()) null else "Tanggal Konsul tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong",
        )

        updateUIState = updateUIState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun updateData(){
        val currentEvent = updateUIState.jadwalEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryJadwal.updateJadwal(currentEvent.toJadwalEntity())
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data berhasil diupdate",
                        jadwalEvent = JadwalEvent(),
                        isEntryValid = FormJadwalErrorState()
                    )
                } catch (e: Exception) {
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data gagal diupdate"
                    )
                }
            }
        } else {
            updateUIState = updateUIState.copy(
                snackBarMessage = "Data gagal diupdate"
            )
        }
    }

    fun resetSnackBarMessage(){
        updateUIState = updateUIState.copy(snackBarMessage = null)
    }
}

fun Jadwal.toUIStateJadwal(): JadwalUIState = JadwalUIState(
    jadwalEvent = this.toDetailJadwalUiEvent()
)