package com.example.tugasakhira12.ui.PagePendaftaran.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugasakhira12.model.Kursus
import com.example.tugasakhira12.model.Siswa
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.CustomNavigation.DynamicDropdownTextField
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.FormErrorStatePendf
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.InsertPendUiEvent
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.InsertPendUiState
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.InsertPendViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.launch

object DestinasiInsertPendaftaran : DestinasiNavigation {
    override val route = "insertpendaftaran"
    override val titleRes = "Tambah Pendaftaran"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertPendaftaran(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPendViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val pendUiState = viewModel.pendfuiState
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(pendUiState.snackBarMessage) {
        pendUiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiInsertPendaftaran.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack,
                onRefresh = {
                    viewModel.clearErrorMessages()
                }
            )
        }
    ){innerPadding ->

        EntryBody(
            insertPendUiState = viewModel.pendfuiState,
            listSiswa = viewModel.listSiswa,
            listKursus = viewModel.listKursus,
            errorState = pendUiState.isEntryValid,
            onPendfValueChange = viewModel::updateInsertPendState,
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
fun EntryBody(
    insertPendUiState: InsertPendUiState,
    onPendfValueChange: (InsertPendUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    listSiswa: List<Siswa>,
    listKursus: List<Kursus>,
    errorState: FormErrorStatePendf = FormErrorStatePendf(),
    modifier: Modifier = Modifier,
    enabled: Boolean = true

    ){
    val status = listOf("Berhasil", "Tidak Berhasil")

    Column(
        modifier = modifier.padding(12.dp)
    ){

        OutlinedTextField(
            value = insertPendUiState.insertPendUiEvent.idPendaftaran,
            onValueChange = {onPendfValueChange(insertPendUiState.insertPendUiEvent.copy(idPendaftaran = it)) },
            label = { Text("Id Pendaftaran") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Info,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Id Pendaftaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.idPendaftaran ?: "", //digunakan untuk memunculkan pesan errornya
            color = Color.Red
        )
        DynamicDropdownTextField(
            label = "siswa",
            selectedValue = insertPendUiState.insertPendUiEvent.namaSiswa,
            listItems = listSiswa.map { it.namaSiswa },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Person,
                    contentDescription = "")
            },
            onValueChanged = {
                val selectedNama = listSiswa.find { siswa -> siswa.namaSiswa == it }
                onPendfValueChange(
                    insertPendUiState.insertPendUiEvent.copy(
                        namaSiswa = it,
                        idSiswa = selectedNama?.idSiswa.orEmpty(),
                    )
                )
            },
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.idSiswa ?: "", //digunakan untuk memunculkan pesan errornya
            color = Color.Red
        )
        DynamicDropdownTextField(
            label = "kursus",
            selectedValue = insertPendUiState.insertPendUiEvent.namaKursus,
            listItems = listKursus.map { it.namaKursus },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.AccountBox,
                    contentDescription = "")
            },
            onValueChanged = {
                val selectedKursus = listKursus.find { kursus -> kursus.namaKursus == it }
                onPendfValueChange(
                    insertPendUiState.insertPendUiEvent.copy(
                        namaKursus = it,
                        idKursus = selectedKursus?.idKursus.orEmpty()
                    )
                )
            },
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.idKursus ?: "", //digunakan untuk memunculkan pesan errornya
            color = Color.Red
        )
        OutlinedTextField(
            value = insertPendUiState.insertPendUiEvent.tglPendaftaran,
            onValueChange = {onPendfValueChange(insertPendUiState.insertPendUiEvent.copy(tglPendaftaran = it)) },
            label = { Text("tanggal pendaftaran") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.DateRange,
                    contentDescription = "")
            },
            placeholder = { Text("masukkan tanggal pendaftaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.tglPendaftaran ?: "", //digunakan untuk memunculkan pesan errornya
            color = Color.Red
        )

        Text("Status", style = MaterialTheme.typography.bodyLarge)
        Row() {
            status.forEach { status ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    androidx.compose.material3.RadioButton(
                        selected = insertPendUiState.insertPendUiEvent.status == status,
                        onClick = { onPendfValueChange(insertPendUiState.insertPendUiEvent.copy(status = status)) }
                    )
                    Text(
                        text = status,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Text(
            text = errorState.status ?: "", //digunakan untuk memunculkan pesan errornya
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


