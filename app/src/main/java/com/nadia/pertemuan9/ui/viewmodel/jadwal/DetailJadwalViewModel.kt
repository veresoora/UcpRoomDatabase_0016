package com.nadia.pertemuan9.ui.viewmodel.jadwal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadia.pertemuan9.data.entity.Jadwal
import com.nadia.pertemuan9.repository.RepositoryJadwal
import com.nadia.pertemuan9.ui.navigation.AlamatDetailJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailJadwalViewModel (
    savedStateHandle: SavedStateHandle,
    private val repositoryJadwal: RepositoryJadwal
) : ViewModel(){
    private val _idJadwal: Int = checkNotNull(savedStateHandle[AlamatDetailJadwal.IDJWL])

    val detailJadwalUiState: StateFlow<DetailUiStateJadwal> = repositoryJadwal.getJadwal(_idJadwal)
        .filterNotNull()
        .map {
            DetailUiStateJadwal(
                detailJadwalUiEvent = it.toDetailJadwalUiEvent(),
                isLoading = false,
            )
        }
        .onStart {
            emit(DetailUiStateJadwal(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailUiStateJadwal(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi kesalahan",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailUiStateJadwal(
                isLoading = true
            )
        )

    fun deleteMhs() {
        detailJadwalUiState.value.detailJadwalUiEvent.toJadwalEntity().let {
            viewModelScope.launch {
                repositoryJadwal.deleteJadwal(it)
            }
        }
    }
}

data class DetailUiStateJadwal(
    val detailJadwalUiEvent: JadwalEvent = JadwalEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    val isUiEventEmpty: Boolean
        get() = detailJadwalUiEvent == JadwalEvent()

    val isUiEventNotEmpty: Boolean
        get() = detailJadwalUiEvent != JadwalEvent()
}

fun Jadwal.toDetailJadwalUiEvent(): JadwalEvent {
    return JadwalEvent(
        idjadwal = idjadwal,
        dokter = dokter,
        namapasien = namapasien,
        nomorhp = nomorhp,
        tglkonsul = tglkonsul,
        status = status
    )
}