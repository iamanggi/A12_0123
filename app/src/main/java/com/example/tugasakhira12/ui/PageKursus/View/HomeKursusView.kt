package com.example.tugasakhira12.ui.PageKursus.View

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugasakhira12.R
import com.example.tugasakhira12.model.Kursus
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.CustomNavigation.CustomBottomAppBar
import com.example.tugasakhira12.ui.PageKursus.ViewModel.HomeKursusViewModel
import com.example.tugasakhira12.ui.PageKursus.ViewModel.KursusUiState
import com.example.tugasakhira12.ui.PagesInstruktur.View.OnError
import com.example.tugasakhira12.ui.PagesInstruktur.View.OnLoading
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation

object DestinasiHomeKursus : DestinasiNavigation {
    override val route = "kursus"
    override val titleRes = "Halaman Kursus"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KursusScreen(
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
    viewModel: HomeKursusViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val showDialog = remember { mutableStateOf(false) } // State untuk dialog konfirmasi
    val kursusToDelete = remember { mutableStateOf<Kursus?>(null) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.getKrs() // Memanggil fungsi untuk mengambil data kursus
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiHomeKursus.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getKrs()
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Kursus")
            }
        },
    ) { innerPadding ->
        // Use a Column with verticalScroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Ensure this is applied
        ) {
            SearchBar(
                query = viewModel.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) }
            )

            KursusStatus(
                kursusUiState = viewModel.krsUIState,
                retryAction = { viewModel.getKrs() },
                modifier = Modifier,
                onDetailClick = onDetailClick,
                onDeleteClick = { kursus ->
                    kursusToDelete.value = kursus
                    showDialog.value = true
                },
                onEditClick = navigateToEdit,
            )

            // Dialog Konfirmasi Hapus
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Apakah Anda yakin ingin menghapus data ${kursusToDelete.value?.namaKursus}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            kursusToDelete.value?.let {
                                viewModel.deleteKrs(it.idKursus)
                                viewModel.getKrs()
                            }
                            showDialog.value = false
                        }) {
                            Text("Hapus")
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
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.Gray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.padding( 5.dp)
        )
        TextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            label = { Text("Cari Kursus") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@Composable
fun KursusStatus(
    kursusUiState: KursusUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Kursus) -> Unit = {},
    onEditClick: (String) -> Unit = {}
) {
    when (kursusUiState) {
        is KursusUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is KursusUiState.Success -> {

            val filteredKursus = kursusUiState.kursus

            if (filteredKursus.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Kursus")
                }
            } else {
                KursusLayout(
                    kursus = filteredKursus,
                    modifier = modifier.fillMaxSize(),
                    onDetailClick = { onDetailClick(it) },
                    onDeleteClick = { onDeleteClick(it) },
                )
            }
        }
        is KursusUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun KursusLayout(
    kursus: List<Kursus>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Kursus) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        kursus.forEach { kursusItem ->
            KursusCard(
                kursus = kursusItem,
                modifier = Modifier.fillMaxWidth(),
                onDeleteClick = { onDeleteClick(kursusItem) },
                onDetailClick = { onDetailClick(kursusItem.idKursus) }
            )
        }
    }
}


@Composable
fun KursusCard(
    kursus: Kursus,
    modifier: Modifier = Modifier,
    onDeleteClick: (Kursus) -> Unit,
    onDetailClick: (String) -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onDetailClick(kursus.idKursus) }
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = kursus.namaKursus.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
                IconButton(
                    onClick = { onDeleteClick(kursus) },
                    modifier = Modifier.size(28.dp) // Mengatur ukuran tombol hapus
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Informasi lainnya tetap sama
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Deskripsi: ${kursus.deskripsi}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kategori: ${kursus.kategori}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Harga: ${kursus.harga}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Nama Instruktur: ${kursus.namaInstruktur}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
