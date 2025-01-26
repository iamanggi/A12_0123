package com.example.tugasakhira12.ui.PageKursus.View

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
import com.example.tugasakhira12.ui.PageKursus.ViewModel.UpdateKursusViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.launch

object DestinasiUpdateKursus : DestinasiNavigation {
    override val route = "IdKursus_Update"
    override val titleRes = "Update Kursus"
    const val IdKursus = "IdKursus"
    val routeWithArgs = "$route/{$IdKursus}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateKursusView(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateKursusViewModel = viewModel(factory = PenyediaViewModel.Factory)

){
    val krsUiState = viewModel.krsUiState
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(krsUiState.snackBarMessage) {
        krsUiState.snackBarMessage?.let { message ->
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
        viewModel.loadInstruktur() // Pastikan Anda memiliki metode ini di ViewModel
    }
    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdateKursus.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ){ innerPadding ->
        FormInput(
            insertKursusUiState = viewModel.krsUiState,
            onValueChange = viewModel::updateInsertKursusState,
            errorState = krsUiState.isEntryValid,
            listInstruktur = viewModel.listInstruktur,
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