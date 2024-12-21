package com.nadia.pertemuan9.ui.view.jadwal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadia.pertemuan9.ui.customvidget.DynamicSelectedTextField
import com.nadia.pertemuan9.ui.viewmodel.PenyediaViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.FormJadwalErrorState
import com.nadia.pertemuan9.ui.viewmodel.jadwal.JadwalEvent
import com.nadia.pertemuan9.ui.viewmodel.jadwal.JadwalUIState
import com.nadia.pertemuan9.ui.viewmodel.jadwal.JadwalViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun InsertJadwalView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JadwalViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Column(
        modifier = Modifier
            .background(color = Color(0xFF00AAEC))
            .fillMaxSize()
    ) {
        IconButton(onClick = onBack, modifier = Modifier.padding(top = 30.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = Modifier.size(35.dp),
                tint = Color.White
            )
        }

        Card (
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tambah Jadwal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )

                InsertBodyJadwal (
                    uiState = uiState,
                    onValueChange = { updatedEvent ->
                        viewModel.UpdateState(updatedEvent)
                    },
                    onClick = {
                        coroutineScope.launch {
                            if (viewModel.validateFields()){
                                viewModel.saveData()
                                delay(500)
                                withContext(Dispatchers.Main) {
                                    onNavigate()
                                }
                            }
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun InsertBodyJadwal(
    modifier: Modifier = Modifier,
    onValueChange: (JadwalEvent) -> Unit,
    uiState: JadwalUIState,
    onClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormJadwal(
            jadwalEvent = uiState.jadwalEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 50.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF00AAEC),
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color(0xFF00AAEC)
            )
        ) {
            Text("Simpan")
        }
    }
}

@Composable
fun FormJadwal(
    jadwalEvent: JadwalEvent = JadwalEvent(),
    onValueChange: (JadwalEvent) -> Unit,
    errorState: FormJadwalErrorState = FormJadwalErrorState(),
    viewModel: JadwalViewModel = viewModel(),
    modifier: Modifier = Modifier
){
    var chosenDropdown by remember { mutableStateOf("") }

    var pilihDokter by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.namapasien,
            onValueChange = {
                onValueChange(jadwalEvent.copy(namapasien = it))
            },
            label = {
                Text("Nama Pasien")
            },
            isError = errorState.namapasien != null,
            placeholder = {
                Text("Masukkan Nama Pasien")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = errorState.namapasien ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.nomorhp,
            onValueChange = {
                onValueChange(jadwalEvent.copy(namapasien = it))
            },
            label = {
                Text("Nomor Hp Pasien")
            },
            isError = errorState.namapasien != null,
            placeholder = {
                Text("Masukkan Nomor Hp Pasien")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = errorState.nomorhp ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect (Unit) {
            viewModel.listNamaDokter.collect { namaDokter ->
                pilihDokter = namaDokter.map { it.nama }
            }
        }

        DynamicSelectedTextField(
            selectedValue = chosenDropdown,
            options = pilihDokter,
            label = "Pilih Nama Dokter",
            onValueChangedEvent = {
                onValueChange(jadwalEvent.copy(dokter = it))
                chosenDropdown = it
            }
        )
        Text(
            text = errorState.dokter ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.tglkonsul,
            onValueChange = {
                onValueChange(jadwalEvent.copy(tglkonsul = it))
            },
            label = {
                Text("Tanggal Konsul")
            },
            isError = errorState.tglkonsul != null,
            placeholder = {
                Text("Masukkan Tanggal Konsul")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = errorState.tglkonsul ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.status,
            onValueChange = {
                onValueChange(jadwalEvent.copy(status = it))
            },
            label = {
                Text("Status Penanganan")
            },
            isError = errorState.status != null,
            placeholder = {
                Text("Masukkan Status Penanganan")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = errorState.status ?: "",
            color = Color.Red
        )
    }
}