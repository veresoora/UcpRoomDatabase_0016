package com.nadia.pertemuan9.ui.view.jadwal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadia.pertemuan9.ui.viewmodel.PenyediaViewModel
import com.nadia.pertemuan9.ui.viewmodel.jadwal.UpdateJadwalViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UpdateJadwalView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    viewModel: UpdateJadwalViewModel = viewModel(factory = PenyediaViewModel.Factory),
    modifier: Modifier = Modifier
){
    val uiJadwalState = viewModel.updateUIState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect (uiJadwalState.snackBarMessage) {
        println("LaunchedEffect triggered")
        uiJadwalState.snackBarMessage?.let { message ->
            println("Launching coroutine: $message")
            coroutineScope.launch {
                println("Launching coroutine for snackbar")
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
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
                    text = "Update Jadwal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )

                InsertBodyJadwal (
                    uiState = uiJadwalState,
                    onValueChange = { updatedEvent ->
                        viewModel.updateState(updatedEvent)
                    },
                    onClick = {
                        coroutineScope.launch {
                            if (viewModel.validateFields()){
                                viewModel.updateData()
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