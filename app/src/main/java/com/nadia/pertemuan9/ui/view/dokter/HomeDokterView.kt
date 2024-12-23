package com.nadia.pertemuan9.ui.view.dokter

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.LocationOn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadia.pertemuan9.R
import com.nadia.pertemuan9.ui.customvidget.TopAppBar
import com.nadia.pertemuan9.data.entity.Dokter
import com.nadia.pertemuan9.ui.viewmodel.PenyediaViewModel
import com.nadia.pertemuan9.ui.viewmodel.dokter.HomeDokterUiState
import com.nadia.pertemuan9.ui.viewmodel.dokter.HomeDokterViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeDokterView(
    viewModel: HomeDokterViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onAddDokter: () -> Unit = { },
    onDetailClick: (String) -> Unit = { },
    navigateLihatJadwal: () -> Unit = { },
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
            navigateKeDokter = onAddDokter,
            navigateKeJadwal = navigateLihatJadwal,
            showButtonDokter = true,
            showButtonJadwal = true,
            showSearch = true,
            titleSearch = "Cari dokter",
            titleButtonDokter = "Tambah Dokter",
            titleButtonJadwal = "Lihat Jadwal"
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
                    text = "Daftar Dokter",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp),
                    fontSize = 30.sp,
                    color = Color.Black
                )

                val homeDokterUiState by viewModel.homeDokterUiState.collectAsState()

                BodyHomeDokterView(
                    homeDokterUiState = homeDokterUiState,
                    onClick = {
                        onDetailClick(it)
                    },
                )
            }
        }
    }
}

@Composable
fun BodyHomeDokterView(
    homeDokterUiState: HomeDokterUiState,
    onClick: (String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() } // Snackbar state
    when {
        homeDokterUiState.isLoading -> {
            // Menampilkan indikator loading
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF00AAEC))
            }
        }

        homeDokterUiState.isError -> {
            LaunchedEffect(homeDokterUiState.errorMessage) {
                homeDokterUiState.errorMessage?.let { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message) //Tampilkan Snackbar
                    }
                }
            }
        }

        homeDokterUiState.dokterList.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada data dokter.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        else -> {
            ListDokter(
                listDokter = homeDokterUiState.dokterList,
                onClick = {
                    onClick(it)
                },
                modifier = modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun ListDokter(
    listDokter: List<Dokter>,
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
            items = listDokter,
            itemContent = { dokter ->
                CardDokter(
                    dokter = dokter,
                    onClick = {
                        onClick(dokter.id.toString())
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDokter(
    dokter: Dokter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    fun spesialisColor(spesialis: String): Color {
        return when (spesialis) {
            "Dokter Spesialis Umum" -> Color(0xFF42A5F5)
            "Dokter Spesialis Anak" -> Color(0xFFAB47BC)
            "Dokter Spesialis Bedah" -> Color(0xFF26A69A)
            else -> Color(0xFF757575)
        }
    }

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
                        text = dokter.nama,
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
                        text = dokter.spesialis,
                        fontWeight = FontWeight.Bold,
                        color = spesialisColor(dokter.spesialis)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Sharp.LocationOn,
                        contentDescription = "",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = dokter.klinik,
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
                        painter = painterResource(id = R.drawable.time) ,
                        contentDescription = "",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = dokter.jamKerja,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
