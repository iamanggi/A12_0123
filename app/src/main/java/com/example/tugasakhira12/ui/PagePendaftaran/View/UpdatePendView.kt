package com.example.tugasakhira12.ui.PagePendaftaran.View

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.UpdatePendViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.launch

object DestinasiUpdatePendaftaran : DestinasiNavigation {
    override val route = "IdPendaftaran_Update"
    override val titleRes = "Update Pendaftaran"
    const val IdPendaftaran = "IdPendaftaran"
    val routeWithArgs = "$route/{$IdPendaftaran}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePendaftaranView(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePendViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val pendUiState = viewModel.penUiState
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(pendUiState.snackBarMessage) {
        pendUiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
                viewModel.resetSnackBarMessage()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadPendaftaran() // Pastikan Anda memiliki metode ini di ViewModel
    }
    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdatePendaftaran.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ){ innerPadding ->
        EntryBody(
            onPendfValueChange = viewModel::updateInsertPendState,
            insertPendUiState = viewModel.penUiState,
            listSiswa = viewModel.listSiswa,
            listKursus = viewModel.listKursus,
            errorState = pendUiState.isEntryValid,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateKursus()
                    navigateBack()
                }
            },
            modifier = modifier
                .padding(innerPadding)
        )

    }
}