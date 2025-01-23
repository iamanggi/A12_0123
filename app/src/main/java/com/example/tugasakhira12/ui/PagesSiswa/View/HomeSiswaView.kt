package com.example.tugasakhira12.ui.PagesSiswa.View

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
import com.example.tugasakhira12.model.Siswa
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.CustomNavigation.CustomBottomAppBar
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.HomeSiswaViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.SiswaUiState
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.delay

object DestinasiHome : DestinasiNavigation {
    override val route = "siswa"
    override val titleRes = "Halaman Siswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiswaScreen(
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
    viewModel: HomeSiswaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

    val showDialog = remember { mutableStateOf(false) }
    val siswaToDelete = remember { mutableStateOf<Siswa?>(null) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.getSiswa() // Memanggil fungsi untuk mengambil data Siswa
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiHome.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getSiswa()
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Siswa")
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
            SiswaStatus(
                siswaUiState = viewModel.mhsUIState,
                retryAction = { viewModel.getSiswa() },
                modifier = Modifier,
                onDetailClick = onDetailClick,
                onDeleteClick = { siswa ->
                    siswaToDelete.value = siswa
                    showDialog.value = true
                },
                onEditClick = navigateToEdit
            )

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Apakah Anda yakin ingin menghapus data ${siswaToDelete.value?.namaSiswa}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            siswaToDelete.value?.let {
                                viewModel.deleteMhs(it.idSiswa)
                                viewModel.getSiswa()
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
fun SiswaStatus(
    siswaUiState: SiswaUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Siswa) -> Unit = {},
    onEditClick: (String) -> Unit = {}
) {
    when (siswaUiState) {
        is SiswaUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is SiswaUiState.Success -> {
            if (siswaUiState.siswa.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Siswa")
                }
            } else {
                SiswaLayout(
                    siswa = siswaUiState.siswa,
                    modifier = modifier.fillMaxSize(),
                    onDetailClick = { onDetailClick(it) },
                    onDeleteClick = { onDeleteClick(it) },
                )
            }
        }
        is SiswaUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun SiswaLayout(
    siswa: List<Siswa>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Siswa) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Row
        HeaderRow()

        // Data Rows
        siswa.forEach { siswaItem ->
            SiswaCard(
                siswa = siswaItem,
                modifier = Modifier.fillMaxWidth(),
                onDeleteClick = { onDeleteClick(siswaItem) },
                onDetailClick = { onDetailClick(siswaItem.idSiswa) }
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
            text = "ID",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(0.7f)
                .padding(8.dp)
        )
        Text(
            text = "Nama Siswa",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(2f)
                .padding(8.dp)
        )
        Text(
            text = "Email",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(2f)
                .padding(8.dp)
        )
        Text(
            text = "Nomor Tlpn",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1.5f)
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
fun SiswaCard(
    siswa: Siswa,
    modifier: Modifier = Modifier,
    onDeleteClick: (Siswa) -> Unit,
    onDetailClick: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F7))
            .border(1.dp, Color.Gray)
            .padding(6.dp)
            .clickable { onDetailClick(siswa.idSiswa) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = siswa.idSiswa,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1, // Membatasi hanya 1 baris
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(1f)
                .padding(6.dp)
        )
        Text(
            text = siswa.namaSiswa,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(2f)
                .padding(6.dp)
        )
        Text(
            text = siswa.email,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(2f)
                .padding(6.dp)
        )
        Text(
            text = siswa.nomorTlp,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(1.5f)
                .padding(6.dp)
        )
        IconButton(
            onClick = { onDeleteClick(siswa) },
            modifier = Modifier.weight(0.8f)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Hapus Siswa"
            )
        }
    }
}


