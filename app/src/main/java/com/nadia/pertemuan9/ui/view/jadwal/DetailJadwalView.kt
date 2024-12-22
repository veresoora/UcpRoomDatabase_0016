package com.nadia.pertemuan9.ui.view.jadwal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadia.pertemuan9.data.entity.Jadwal
import com.nadia.pertemuan9.ui.view.dokter.InsertBodyDokter
import com.nadia.pertemuan9.ui.viewmodel.PenyediaViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.DetailJadwalViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.DetailUiStateJadwal
import com.nadia.pertemuan9.ui.viewmodel.jadwal.toJadwalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DetailJadwalView (
    modifier: Modifier = Modifier,
    viewModel: DetailJadwalViewModel = viewModel (factory = PenyediaViewModel.Factory),
    onBack: () -> Unit = { },
    onEditClick: (Int) -> Unit = { },
    onDeleteClick: () -> Unit = { }
){
    Box(
        modifier = Modifier
            .background(color = Color(0xFF00AAEC))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
                        text = "Detail Jadwal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    val detailJadwalUiState by viewModel.detailJadwalUiState.collectAsState()

                    BodyDetailJadwal(
                        detailJadwalUiState = detailJadwalUiState,
                        onDeleteClick = {
                            viewModel.deleteMhs()
                            onDeleteClick()
                        }
                    )
                }

            }
        }

        FloatingActionButton(
            onClick = {
                onEditClick(viewModel.detailJadwalUiState.value.detailJadwalUiEvent.idjadwal)
            },
            shape = MaterialTheme.shapes.medium,
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Jadwal"
            )
        }

    }
}

@Composable
fun BodyDetailJadwal (
    modifier: Modifier = Modifier,
    detailJadwalUiState: DetailUiStateJadwal = DetailUiStateJadwal(),
    onDeleteClick: () -> Unit = { }
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when {
        detailJadwalUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator() // Tampilkan loading
            }
        }

        detailJadwalUiState.isUiEventNotEmpty -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailJadwal(
                    jadwal = detailJadwalUiState.detailJadwalUiEvent.toJadwalEntity(),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Button (
                    onClick = {
                        deleteConfirmationRequired = true
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 50.dp)
                ) {
                    Text(text = "Delete")
                }
                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDeleteClick()
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        detailJadwalUiState.isUiEventEmpty -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ItemDetailJadwal (
    modifier: Modifier = Modifier,
    jadwal: Jadwal
){
    Card (
        modifier = modifier
            .fillMaxWidth (),
        colors = CardDefaults.cardColors (
            containerColor = Color(0xFF00AAEC),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            ComponentDetailJadwal (judul = "ID Jadwal", isinya = jadwal.idjadwal.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailJadwal (judul = "Nama Pasien", isinya = jadwal.namapasien)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailJadwal (judul = "Nomor Hp Pasien", isinya = jadwal.nomorhp)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailJadwal (judul = "Nama Dokter", isinya = jadwal.dokter)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailJadwal (judul = "Tanggal Konsul", isinya = jadwal.tglkonsul)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailJadwal (judul = "Status Penanganan", isinya = jadwal.status)
        }
    }
}

@Composable
fun ComponentDetailJadwal (
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column (
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black

        )
        Text(
            text = isinya, fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun DeleteConfirmationDialog (
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier =
        Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        modifier = modifier,
        dismissButton = {
            TextButton (onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}