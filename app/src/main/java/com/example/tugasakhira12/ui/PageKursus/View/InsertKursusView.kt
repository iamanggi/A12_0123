package com.example.tugasakhira12.ui.PageKursus.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugasakhira12.model.Instruktur
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.CustomNavigation.DynamicDropdownTextField
import com.example.tugasakhira12.ui.PageKursus.ViewModel.FormErrorState
import com.example.tugasakhira12.ui.PageKursus.ViewModel.InsertKursusUiEvent
import com.example.tugasakhira12.ui.PageKursus.ViewModel.InsertKursusUiState
import com.example.tugasakhira12.ui.PageKursus.ViewModel.InsertKursusViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.launch

object DestinasiInsertKursus : DestinasiNavigation {
    override val route = "insertkursus"
    override val titleRes = "Tambah Kursus"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertKursus(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertKursusViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val krsUiState = viewModel.krsuiState
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    //Observasi perubahan snackbarMessage
    LaunchedEffect(krsUiState.snackBarMessage) {
        krsUiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CostumeTopAppBar(
                title = DestinasiInsertKursus.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack,
                onRefresh = {
                    viewModel.clearErrorMessages() // Reset error messages
                }

            )
        }
    ){innerPadding ->
        FormInput(
            insertKursusUiState = viewModel.krsuiState,
            onValueChange = viewModel::updateInsertKursusState,
            errorState = krsUiState.isEntryValid,
            listInstruktur = viewModel.listInstruktur,
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
fun FormInput(
    onValueChange: (InsertKursusUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    insertKursusUiState: InsertKursusUiState,
    errorState: FormErrorState = FormErrorState(),
    enabled: Boolean = true,
    listInstruktur: List<Instruktur>,
    onSaveClick: () -> Unit
    ) {

    val kategori = listOf("Soshum", "Saintek")

    Column(
        modifier = modifier.padding(10.dp),
    ) {
        OutlinedTextField(
            value = insertKursusUiState.insertKursusUiEvent.idKursus,
            onValueChange = { onValueChange(insertKursusUiState.insertKursusUiEvent.copy(idKursus = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Id Kursus") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Info,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Id Kursus") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.idKursus ?: "", //digunakan untuk memunculkan pesan errornya
            color = Color.Red
        )

        OutlinedTextField(
            value = insertKursusUiState.insertKursusUiEvent.namaKursus,
            onValueChange = { onValueChange(insertKursusUiState.insertKursusUiEvent.copy(namaKursus = it)) },
            label = { Text("Nama Kursus") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Person,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Nama Kursus") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.namaKursus ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertKursusUiState.insertKursusUiEvent.deskripsi,
            onValueChange = { onValueChange(insertKursusUiState.insertKursusUiEvent.copy(deskripsi = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Deskripsi") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Email,
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
        Text("Kategori", style = MaterialTheme.typography.bodyLarge)
        Row() {
            kategori.forEach { kategori ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    androidx.compose.material3.RadioButton(
                        selected = insertKursusUiState.insertKursusUiEvent.kategori == kategori,
                        onClick = { onValueChange(insertKursusUiState.insertKursusUiEvent.copy(kategori = kategori)) }
                    )
                    Text(
                        text = kategori,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Text(
            text = errorState.kategori ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertKursusUiState.insertKursusUiEvent.harga.toString(), // Konversi Double ke String
            onValueChange = {
                val newHarga = it.toDoubleOrNull() // Coba konversi input ke Double
                if (newHarga != null) {
                    onValueChange(insertKursusUiState.insertKursusUiEvent.copy(harga = newHarga))
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Harga") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Icon Email"
                )
            },
            placeholder = { Text("Masukkan Harga") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.hargaError ?: "",
            color = Color.Red
        )

        DynamicDropdownTextField(
            label = "instruktur",
            selectedValue = insertKursusUiState.insertKursusUiEvent.namaInstruktur,
            listItems = listInstruktur.map { it.namaInstruktur },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.AccountBox,
                    contentDescription = "")
            },
            onValueChanged = {
                val selectedInst = listInstruktur.find { inst -> inst.namaInstruktur == it }
                onValueChange(
                    insertKursusUiState.insertKursusUiEvent.copy(
                        namaInstruktur = it,
                        idInstruktur = selectedInst?.idInstruktur.orEmpty()
                    )
                )
            },
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.namaInstruktur ?: "",
            color = Color.Red
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