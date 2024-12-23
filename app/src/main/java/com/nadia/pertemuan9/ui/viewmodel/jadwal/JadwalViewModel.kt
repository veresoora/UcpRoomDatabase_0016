package com.nadia.pertemuan9.ui.viewmodel.jadwal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadia.pertemuan9.data.entity.Dokter
import com.nadia.pertemuan9.data.entity.Jadwal
import com.nadia.pertemuan9.repository.RepositoryJadwal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class JadwalViewModel(
    private val repositoryJadwal: RepositoryJadwal
): ViewModel(){
    var uiState by mutableStateOf(JadwalUIState())

    fun UpdateState(jadwalEvent: JadwalEvent){
        uiState = uiState.copy(
            jadwalEvent = jadwalEvent
        )
    }

    fun validateFields(): Boolean {
        val event = uiState.jadwalEvent
        val errorState = FormJadwalErrorState(
            dokter = if (event.dokter.isNotEmpty()) null else "Nama Dokter tidak boleh kosong",
            namapasien = if (event.namapasien.isNotEmpty()) null else "Nama Pasien tidak boleh kosong",
            nomorhp = if (event.nomorhp.isNotEmpty()) null else "Nomor Hp Pasien tidak boleh kosong",
            tglkonsul = if (event.tglkonsul.isNotEmpty()) null else "Tanggal Konsul tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong",
        )

        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun saveData(){
        val currentEvent = uiState.jadwalEvent

        if (validateFields()){
            viewModelScope.launch {
                try {
                    repositoryJadwal.insertJadwal(currentEvent.toJadwalEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data berhasil disimpan",
                        jadwalEvent = JadwalEvent(),
                        isEntryValid = FormJadwalErrorState()
                    )
                } catch (e: Exception){
                    uiState = uiState.copy(
                        snackBarMessage = "Data gagal disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy(
                snackBarMessage = "Input tidak valid. Periksa kembali data anda"
            )
        }
    }

    fun resetSnackBarMessage(){
        uiState = uiState.copy(snackBarMessage = null)
    }

    fun namaDokterList(){
        viewModelScope.launch {
            repositoryJadwal.getNamaDokter().collect{dktr ->
                uiState = uiState.copy(namaDokterList = dktr)
            }
        }
    }

    init {
        namaDokterList()
    }
}

data class JadwalEvent(
    val idjadwal: Int = 0,
    val dokter: String = "",
    val namapasien: String = "",
    val nomorhp: String = "",
    val tglkonsul: String = "",
    val status: String = "",
)

fun JadwalEvent.toJadwalEntity(): Jadwal = Jadwal(
    idjadwal = idjadwal,
    dokter = dokter,
    namapasien = namapasien,
    nomorhp = nomorhp,
    tglkonsul = tglkonsul,
    status = status
)

data class FormJadwalErrorState(
    val dokter: String? = null,
    val namapasien: String? = null,
    val nomorhp: String? = null,
    val tglkonsul: String? = null,
    val status: String? = null,
){
    fun isValid(): Boolean {
        return dokter == null
                && namapasien == null
                && nomorhp == null
                && tglkonsul == null
                && status == null
    }
}

data class JadwalUIState(
    val jadwalEvent: JadwalEvent = JadwalEvent(),
    val isEntryValid: FormJadwalErrorState = FormJadwalErrorState(),
    val snackBarMessage: String? = null,
    val namaDokterList: List<Dokter> = emptyList()
)