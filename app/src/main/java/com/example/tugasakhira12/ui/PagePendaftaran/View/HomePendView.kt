package com.example.tugasakhira12.ui.PagePendaftaran.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.MutableState
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
    val selectedCategory = remember { mutableStateOf("Semua") }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterButton("Semua", selectedCategory) { selectedCategory.value = "Semua" }
                FilterButton("Saintek", selectedCategory) { selectedCategory.value = "Saintek" }
                FilterButton("Soshum", selectedCategory) { selectedCategory.value = "Soshum" }
            }

            PendaftaranStatus(
                pendUiState = viewModel.pndfUIState,
                retryAction = { viewModel.getPndftrn() },
                modifier = Modifier,
                onDetailClick = onDetailClick,
                onDeleteClick = { pendf ->
                    pendToDelete.value = pendf
                    showDialog.value = true
                },
                onEditClick = navigateToEdit,
                selectedCategory = selectedCategory.value
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
fun FilterButton(label: String, selectedCategory: MutableState<String>, onClick: () -> Unit) {
    val isSelected = selectedCategory.value == label
    TextButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(2.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(90.dp)
            )
    ) {
        Text(
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PendaftaranStatus(
    pendUiState: PendUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Pendaftaran) -> Unit = {},
    onEditClick: (String) -> Unit = {},
    selectedCategory: String
) {
    when (pendUiState) {
        is PendUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is PendUiState.Success -> {
            if (pendUiState.pendaftaran.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Pendaftaran")
                }
            } else {
                // Filter pendaftaran berdasarkan kategori
                val filteredPendaftaran = if (selectedCategory == "Semua") {
                    pendUiState.pendaftaran
                } else {
                    pendUiState.pendaftaran.filter { it.kategori == selectedCategory }
                }

                PendaftaranLayout(
                    pendaftaran = filteredPendaftaran,
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
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        pendaftaran.forEach { pendItem ->
            PendaftaranCard(
                pendaftaran = pendItem,
                onDeleteClick = { onDeleteClick(pendItem) },
                onDetailClick = { onDetailClick(pendItem.idPendaftaran) }
            )
        }
    }
}

@Composable
fun PendaftaranCard(
    pendaftaran: Pendaftaran,
    modifier: Modifier = Modifier,
    onDeleteClick: (Pendaftaran) -> Unit,
    onDetailClick: (String) -> Unit = {}
) {

    val kategoriImage = when (pendaftaran.kategori) {
        "Saintek" -> painterResource(id = R.drawable.sigma)
        "Soshum" -> painterResource(id = R.drawable.buku)
        else -> painterResource(id = R.drawable.info)
    }

    Card(
        modifier = modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            .clickable { onDetailClick(pendaftaran.idPendaftaran) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = kategoriImage,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ID Pendaftaran: ${pendaftaran.idPendaftaran}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(
                    onClick = { onDeleteClick(pendaftaran) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nama Siswa: ${pendaftaran.namaSiswa}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nama Kursus: ${pendaftaran.namaKursus}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tanggal: ${pendaftaran.tglPendaftaran}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Status: ${pendaftaran.status}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }
    }
}