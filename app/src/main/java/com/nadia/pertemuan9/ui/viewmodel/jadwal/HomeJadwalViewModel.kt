package com.nadia.pertemuan9.ui.viewmodel.jadwal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadia.pertemuan9.data.entity.Jadwal
import com.nadia.pertemuan9.repository.RepositoryJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeJadwalViewModel(
    private val repositoryJadwal: RepositoryJadwal
) : ViewModel() {
    val homeUiStateJadwal: StateFlow<HomeUiStateJadwal> = repositoryJadwal.getAllJadwal()
        .filterNotNull()
        .map {
            HomeUiStateJadwal(
                listJadwal = it.toList(),
                isLoading = false,
            )
        }
        .onStart {
            emit(HomeUiStateJadwal(isLoading = true))
            delay(900)
        }
        .catch {
            emit(
                HomeUiStateJadwal(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message?: "Terjadi kesalah"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiStateJadwal(
                isLoading = true
            )
        )
}

data class HomeUiStateJadwal(
    val listJadwal: List<Jadwal> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)