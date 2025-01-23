package com.example.tugasakhira12.ui.PagesInstruktur.View

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
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.UpdateInstrukturViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.launch

object DestinasiUpdateInstruktur : DestinasiNavigation {
    override val route = "IdInstruktur_Update"
    override val titleRes = "Update Instruktur"
    const val IdInstruktur = "IdInstruktur"
    val routeWithArgs = "$route/{$IdInstruktur}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateInstrukturView(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateInstrukturViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val uiInstState = viewModel.uiInstState
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiInstState.snackBarMessage) {
        uiInstState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
                viewModel.resetSnackBarMessage()
            }
        }
    }
    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdateInstruktur.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ){ innerPadding ->
        EntryBodyInst(
            insUiState = viewModel.uiInstState,
            onInstrukturValueChange = viewModel::updateInsertInstState,
            uiInstState = viewModel.uiInstState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateInstruktur()
                    navigateBack()
                }
            },
            modifier = modifier
                .padding(innerPadding)
        )

    }
}