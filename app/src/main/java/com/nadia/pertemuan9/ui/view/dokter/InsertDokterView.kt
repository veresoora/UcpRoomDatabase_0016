package com.nadia.pertemuan9.ui.view.dokter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadia.pertemuan9.data.objek.SpesialisDokter
import com.nadia.pertemuan9.ui.customvidget.DynamicSelectedTextField
import com.nadia.pertemuan9.ui.viewmodel.PenyediaViewModel
import com.nadia.pertemuan9.ui.viewmodel.dokter.DokterEvent
import com.nadia.pertemuan9.ui.viewmodel.dokter.DokterUiState
import com.nadia.pertemuan9.ui.viewmodel.dokter.DokterViewModel
import com.nadia.pertemuan9.ui.viewmodel.dokter.FormErrorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun InsertDokterView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DokterViewModel = viewModel(factory = PenyediaViewModel.Factory)
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
            .padding(top = 10.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
            }

            Text(
                text = "Tambah Dokter",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.White
            )
            Box {  }
            Box {  }
        }

        Card (
            modifier = Modifier.fillMaxSize()
        ) {
            InsertBodyDokter (
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

@Composable
fun InsertBodyDokter(
    modifier: Modifier = Modifier,
    onValueChange: (DokterEvent) -> Unit,
    uiState: DokterUiState,
    onClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormDokter(
            dokterEvent = uiState.dokterEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
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
fun FormDokter(
    dokterEvent: DokterEvent = DokterEvent(),
    onValueChange: (DokterEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
){
    var chosenDropdown by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.nama,
            onValueChange = {
                onValueChange(dokterEvent.copy(nama = it))
            },
            label = {
                Text("Nama Dokter")
            },
            isError = errorState.nama != null,
            placeholder = {
                Text("Masukkan Nama Dokter")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        DynamicSelectedTextField(
            selectedValue = chosenDropdown,
            options = SpesialisDokter.options,
            label = "Pilih Spesialis",
            onValueChangedEvent = {
                onValueChange(dokterEvent.copy(spesialis = it))
                chosenDropdown = it
            }
        )
        Text(
            text = errorState.spesialis ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.klinik,
            onValueChange = {
                onValueChange(dokterEvent.copy(klinik = it))
            },
            label = {
                Text("Klinik")
            },
            isError = errorState.klinik != null,
            placeholder = {
                Text("Masukkan Klinik")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = errorState.klinik ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.nohp,
            onValueChange = {
                onValueChange(dokterEvent.copy(nohp = it))
            },
            label = {
                Text("Nomor Handphone")
            },
            isError = errorState.nohp != null,
            placeholder = {
                Text("Masukkan Nomor Handphone")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = errorState.nohp ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.jamKerja,
            onValueChange = {
                onValueChange(dokterEvent.copy(jamKerja = it))
            },
            label = {
                Text("Jam Kerja")
            },
            isError = errorState.jamKerja != null,
            placeholder = {
                Text("Masukkan Jam Kerja")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = errorState.jamKerja ?: "",
            color = Color.Red
        )
    }
}