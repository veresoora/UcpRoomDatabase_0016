package com.nadia.pertemuan9.data.objek

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadia.pertemuan9.ui.viewmodel.PenyediaViewModel
import com.nadia.pertemuan9.ui.viewmodel.dokter.HomeDokterViewModel

object NamaDokter {
    @Composable
    fun namaDokter(
        homeDokterViewModel: HomeDokterViewModel = viewModel(factory = PenyediaViewModel.Factory)
    ) : List<String>{
        val dokter by homeDokterViewModel.homeDokterUiState.collectAsState()
        val listdokter = dokter.dokterList.map { it.nama }
        return listdokter
    }
}