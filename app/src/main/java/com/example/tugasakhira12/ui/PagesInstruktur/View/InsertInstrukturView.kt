package com.example.tugasakhira12.ui.PagesInstruktur.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.FormErrorStateInst
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.InsertInsUiState
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.InsertInstUiEvent
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.InsertInstrukturViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.launch

object DestinasiInsertInstruktur : DestinasiNavigation {
    override val route = "insertinstruktur"
    override val titleRes = "Tambah Instruktur"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertInstruktur(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertInstrukturViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiInstState = viewModel.uiInstState
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(uiInstState.snackBarMessage) {
        uiInstState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message) //Tampilkan snackbar
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiInsertInstruktur.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack,
                onRefresh = {
                    viewModel.clearErrorMessages()
                }
            )
        }
    ){innerPadding ->
        EntryBodyInst(
            insUiState = viewModel.uiInstState,
            onInstrukturValueChange = viewModel::updateInsertInstrukturState,
            uiInstState = viewModel.uiInstState,
            onSaveClick = {
                if (viewModel.validateFields()) {
                    coroutineScope.launch {
                        viewModel.saveData()
                        navigateBack()
                    }
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun EntryBodyInst(
    insUiState: InsertInsUiState,
    onInstrukturValueChange: (InsertInstUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    uiInstState: InsertInsUiState,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        FormInputInstruktur(
            insertInstUiEvent = insUiState.insertInstUiEvent,
            errorState = uiInstState.isEntryValid,
            onValueChange = onInstrukturValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan",
                fontSize = 16.sp)
        }
    }
}


@Composable
fun FormInputInstruktur(
    insertInstUiEvent: InsertInstUiEvent,
    onValueChange: (InsertInstUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    errorState: FormErrorStateInst = FormErrorStateInst(),
    enabled: Boolean = true,
    ) {
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        OutlinedTextField(
            value = insertInstUiEvent.idInstruktur,
            onValueChange = { onValueChange(insertInstUiEvent.copy(idInstruktur = it)) },
            label = { Text("Id Instruktur") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Info,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Id Instruktur") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.idInstruktur ?: "", //digunakan untuk memunculkan pesan errornya
            color = Color.Red
        )
        OutlinedTextField(
            value = insertInstUiEvent.namaInstruktur,
            onValueChange = { onValueChange(insertInstUiEvent.copy(namaInstruktur = it)) },
            label = { Text("Nama Instruktur") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Person,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Nama Instruktur") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.namaInstruktur ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertInstUiEvent.email,
            onValueChange = { onValueChange(insertInstUiEvent.copy(email = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = { Text("Email") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Email,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.email ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertInstUiEvent.noTelepon,
            onValueChange = { onValueChange(insertInstUiEvent.copy(noTelepon = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = { Text("Nomor Telepon") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Call,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Nomor Telepon") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.noTelepon ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertInstUiEvent.deskripsi,
            onValueChange = { onValueChange(insertInstUiEvent.copy(deskripsi = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = { Text("Deskripsi") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Star,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Deskripsi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.deskripsi ?: "",
            color = Color.Red
        )
    }
}