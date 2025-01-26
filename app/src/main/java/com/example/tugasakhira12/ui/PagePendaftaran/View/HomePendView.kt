package com.example.tugasakhira12.ui.PagePendaftaran.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugasakhira12.R
import com.example.tugasakhira12.model.Pendaftaran
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.CustomNavigation.CustomBottomAppBar
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.HomePendViewModel
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.PendUiState
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.delay

object DestinasiHomePendaftaran : DestinasiNavigation {
    override val route = "pendaftaran"
    override val titleRes = "Halaman pendaftaran"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendaftaranScreen(
    navigateBack: () -> Unit,
    navigateToltemEntry: () -> Unit,
    navigateToEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    navHome: () -> Unit,
    navSiswa: () -> Unit,
    navInstruktur: () -> Unit,
    navKursus: () -> Unit,
    navPendaftaran: () -> Unit,
    viewModel: HomePendViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

    val showDialog = remember { mutableStateOf(false) }
    val pendToDelete = remember { mutableStateOf<Pendaftaran?>(null) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.getPndftrn() // Memanggil fungsi untuk mengambil data pendaftaran
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiHomePendaftaran.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getPndftrn()
                }
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                onHomeClick = navHome,
                onSiswaClick = navSiswa,
                onInstrukturClick = navInstruktur,
                onKursusClick = navKursus,
                onPendaftaranClick = navPendaftaran,
                selectedItem = 0
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToltemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Pendaftaran")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            PendaftaranStatus(
                pendUiState = viewModel.pndfUIState,
                retryAction = { viewModel.getPndftrn() },
                modifier = Modifier,
                onDetailClick = onDetailClick,
                onDeleteClick = { pendf ->
                    pendToDelete.value = pendf
                    showDialog.value = true
                },
                onEditClick = navigateToEdit
            )

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Apakah Anda yakin ingin menghapus data ${pendToDelete.value?.idPendaftaran}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            pendToDelete.value?.let {
                                viewModel.deletePndftrn(it.idPendaftaran)
                                viewModel.getPndftrn()
                            }
                            showDialog.value = false
                        }) {
                            Text("Hapus" , color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PendaftaranStatus(
    pendUiState: PendUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Pendaftaran) -> Unit = {},
    onEditClick: (String) -> Unit = {}
) {
    when (pendUiState) {
        is PendUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is PendUiState.Success -> {
            if (pendUiState.pendaftaran.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Pendaftaran")
                }
            } else {
                PendaftaranLayout(
                    pendaftaran = pendUiState.pendaftaran,
                    modifier = modifier.fillMaxSize(),
                    onDetailClick = { onDetailClick(it) },
                    onDeleteClick = { onDeleteClick(it) },
                )
            }
        }
        is PendUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier, duration: Long = 1000000000L) {
    LaunchedEffect(Unit) {
        delay(duration)
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.loading1),
            contentDescription = stringResource(R.string.loading)
        )
        Text(
            text = "loading",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun OnError(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.loading2),
            contentDescription = stringResource(R.string.loading_failed)
        )
        Text(
            text = "Data gagal dimuat",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun PendaftaranLayout(
    pendaftaran: List<Pendaftaran>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Pendaftaran) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderRow()

        pendaftaran.forEach { PendItem ->
            PendaftaranCard(
                pendaftaran = PendItem,
                modifier = Modifier.fillMaxWidth(),
                onDeleteClick = { onDeleteClick(PendItem) },
                onDetailClick = { onDetailClick(PendItem.idPendaftaran) }
            )
        }
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp).clip(shape = RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.biruTabel))
            .border(1.dp, Color.Black),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Id",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        Text(
            text = "Id siswa",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1.6f)
                .padding(8.dp)
        )
        Text(
            text = "id Kursus",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1.7f)
                .padding(8.dp)
        )
        Text(
            text = "Tgl",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        Text(
            text = "Status",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(2f)
                .padding(8.dp)
        )
        Text(
            text = "Aksi",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1.2f)
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PendaftaranCard(
    pendaftaran: Pendaftaran,
    modifier: Modifier = Modifier,
    onDeleteClick: (Pendaftaran) -> Unit,
    onDetailClick: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F7))
            .border(1.dp, Color.Gray)
            .padding(6.dp)
            .clickable { onDetailClick(pendaftaran.idPendaftaran) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = pendaftaran.idPendaftaran,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1, // Membatasi hanya 1 baris
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(1f)
                .padding(6.dp)
        )
        Text(
            text = pendaftaran.idSiswa,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(1.6f)
                .padding(6.dp)
        )
        Text(
            text = pendaftaran.idKursus,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(1.7f)
                .padding(6.dp)
        )
        Text(
            text = pendaftaran.tglPendaftaran,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(1f)
                .padding(6.dp)
        )
        Text(
            text = pendaftaran.status,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(2f)
                .padding(6.dp)
        )
        IconButton(
            onClick = { onDeleteClick(pendaftaran) },
            modifier = Modifier.weight(0.8f)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Hapus Pendaftaran"
            )
        }
    }
}


