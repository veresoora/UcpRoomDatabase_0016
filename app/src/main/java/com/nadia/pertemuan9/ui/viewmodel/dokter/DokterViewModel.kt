package com.nadia.pertemuan9.ui.viewmodel.dokter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadia.pertemuan9.data.entity.Dokter
import com.nadia.pertemuan9.repository.RepositoryDokter
import kotlinx.coroutines.launch

class DokterViewModel (
    private val repositoryDokter: RepositoryDokter
): ViewModel(){
    var uiState by mutableStateOf(DokterUiState())

    fun UpdateState(dokterEvent: DokterEvent){
        uiState = uiState.copy(
            dokterEvent = dokterEvent
        )
    }

    fun validateFields(): Boolean {
        val event = uiState.dokterEvent
        val errorState = FormErrorState(
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            spesialis = if (event.spesialis.isNotEmpty()) null else "Spesialis tidak boleh kosong",
            klinik = if (event.klinik.isNotEmpty()) null else "Klinik tidak boleh kosong",
            nohp = if (event.nohp.isNotEmpty()) null else "Nomor Handphone tidak boleh kosong",
            jamKerja = if (event.jamKerja.isNotEmpty()) null else "Jam Kerja tidak boleh kosong",
        )

        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun saveData(){
        val currentEvent = uiState.dokterEvent

        if (validateFields()){
            viewModelScope.launch {
                try {
                    repositoryDokter.insertDokter(currentEvent.toDokterEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data berhasil disimpan",
                        dokterEvent = DokterEvent(),
                        isEntryValid = FormErrorState()
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
}

data class DokterEvent (
    val id: Int = 0,
    val nama: String = "",
    val spesialis: String = "",
    val klinik: String = "",
    val nohp: String = "",
    val jamKerja: String = ""
)

fun DokterEvent.toDokterEntity(): Dokter = Dokter(
    id = id,
    nama = nama,
    spesialis = spesialis,
    klinik = klinik,
    nohp = nohp,
    jamKerja = jamKerja
)

data class FormErrorState(
    val nama: String? = null,
    val spesialis: String? = null,
    val klinik: String? = null,
    val nohp: String? = null,
    val jamKerja: String? = null,
){
    fun isValid(): Boolean {
        return nama == null
                && spesialis == null
                && klinik == null
                && nohp == null
                && jamKerja == null
    }
}

data class DokterUiState (
    val dokterEvent: DokterEvent = DokterEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackBarMessage: String? = null,
)