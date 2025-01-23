package com.example.tugasakhira12.ui.PagesSiswa.View

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
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.FormErrorStateSiswa
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.InsertSiswaViewModel
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.InsertUiEvent
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.InsertUiState
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.launch

object DestinasiInsertSiswa : DestinasiNavigation {
    override val route = "insertsiswa"
    override val titleRes = "Tambah Siswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertSiswa(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertSiswaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
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
                title = DestinasiInsertSiswa.titleRes,
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
            insertUiState = viewModel.uiState,
            onSiswaValueChange = viewModel::updateInsertSiswaState,
            uiState = viewModel.uiState,
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
    insertUiState: InsertUiState,
    onSiswaValueChange: (InsertUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    uiState: InsertUiState,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        FormInput(
            insertUiEvent = insertUiState.insertUiEvent,
            errorState = uiState.isEntryValid,
            onValueChange = onSiswaValueChange,
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
fun FormInput(
    insertUiEvent: InsertUiEvent,
    onValueChange: (InsertUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    errorState: FormErrorStateSiswa = FormErrorStateSiswa(),
    enabled: Boolean = true,
    ) {

    Column(
        modifier = modifier.padding(8.dp),
    ) {
        OutlinedTextField(
            value = insertUiEvent.idSiswa,
            onValueChange = { onValueChange(insertUiEvent.copy(idSiswa = it)) },
            label = { Text("Id Siswa") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Info,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Id Siswa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.idSiswa ?: "", //digunakan untuk memunculkan pesan errornya
            color = Color.Red
        )
        OutlinedTextField(
            value = insertUiEvent.namaSiswa,
            onValueChange = { onValueChange(insertUiEvent.copy(namaSiswa = it)) },
            label = { Text("Nama Siswa") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Person,
                    contentDescription = "")
            },
            placeholder = { Text("Masukkan Nama Siswa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )
        Text(
            text = errorState.namaSiswa ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertUiEvent.email,
            onValueChange = { onValueChange(insertUiEvent.copy(email = it)) },
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
            value = insertUiEvent.nomorTlp,
            onValueChange = { onValueChange(insertUiEvent.copy(nomorTlp = it)) },
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
            text = errorState.nomorTlp ?: "",
            color = Color.Red
        )
    }
}