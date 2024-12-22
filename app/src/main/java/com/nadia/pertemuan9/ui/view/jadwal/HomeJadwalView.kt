package com.nadia.pertemuan9.ui.view.jadwal

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material.icons.sharp.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadia.pertemuan9.data.entity.Jadwal
import com.nadia.pertemuan9.ui.customvidget.TopAppBar
import com.nadia.pertemuan9.ui.viewmodel.PenyediaViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.HomeJadwalViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.HomeUiStateJadwal
import kotlinx.coroutines.launch

@Composable
fun HomejadwalView(
    viewModel: HomeJadwalViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onAddJadwal: () -> Unit = { },
    onDetailClick: (String) -> Unit = { },
    navigateLihatDokter: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .background(color = Color(0xFF00AAEC))
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        TopAppBar(
            title = "Welcome,",
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            navigateKeJadwal = onAddJadwal,
            navigateKeDokter = navigateLihatDokter,
            showButtonDokter = true,
            showButtonJadwal = true,
            showSearch = true,
            titleSearch = "Cari jadwal",
            titleButtonJadwal = "Tambah Jadwal",
            titleButtonDokter = "Lihat Dokter"
        )

        Card (
            modifier = Modifier.fillMaxSize(),
            shape = RectangleShape
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Daftar jadwal",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp),
                    fontSize = 30.sp,
                    color = Color.Black
                )

                val homejadwalUiState by viewModel.homeUiStateJadwal.collectAsState()

                BodyHomejadwalView(
                    homejadwalUiState = homejadwalUiState,
                    onClick = {
                        onDetailClick(it)
                    },
                )
            }
        }
    }
}

@Composable
fun BodyHomejadwalView(
    homejadwalUiState: HomeUiStateJadwal,
    onClick: (String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() } // Snackbar state
    when {
        homejadwalUiState.isLoading -> {
            // Menampilkan indikator loading
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF00AAEC))
            }
        }

        homejadwalUiState.isError -> {
            LaunchedEffect(homejadwalUiState.errorMessage) {
                homejadwalUiState.errorMessage?.let { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message) //Tampilkan Snackbar
                    }
                }
            }
        }

        homejadwalUiState.listJadwal.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada data jadwal.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        else -> {
            Listjadwal(
                listJadwal = homejadwalUiState.listJadwal,
                onClick = {
                    onClick(it)
                },
                modifier = modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun Listjadwal(
    listJadwal: List<Jadwal>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { }
) {
    LazyColumn(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF00AAEC), Color(0xFF007BB8))
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(
            items = listJadwal,
            itemContent = { jadwal ->
                Cardjadwal(
                    jadwal = jadwal,
                    onClick = {
                        onClick(jadwal.idjadwal.toString())
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cardjadwal(
    jadwal: Jadwal,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "",
                modifier = Modifier.size(100.dp),
                tint = Color(0xFF007BB8)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = jadwal.namapasien,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = jadwal.status,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Sharp.DateRange,
                        contentDescription = "",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = jadwal.tglkonsul,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Phone,
                        contentDescription = "",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = jadwal.nomorhp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}