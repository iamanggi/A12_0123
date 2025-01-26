package com.example.tugasakhira12.ui.PageKursus.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugasakhira12.model.Kursus
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.PageKursus.ViewModel.DetailKursusUiState
import com.example.tugasakhira12.ui.PageKursus.ViewModel.DetailKursusViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import java.text.NumberFormat
import java.util.Locale

object DestinasiDetailKursus : DestinasiNavigation {
    override val route = "idKursus_detail"
    override val titleRes = "Detail Kursus"
    const val idKursus = "idKursus"
    val routeWithArgs = "$route/{$idKursus}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKursusView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    detailKursusViewModel: DetailKursusViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        detailKursusViewModel.getKursusById() // Memanggil fungsi untuk mengambil data kursus
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetailKursus.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                onRefresh = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val idKursus = (detailKursusViewModel.detailKursusUiState as? DetailKursusUiState.Success)?.kursus?.idKursus
                    if (idKursus != null) onEditClick(idKursus)
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Kursus",
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            DetailStatusKursus(
                kursusUiState = detailKursusViewModel.detailKursusUiState,
                retryAction = { detailKursusViewModel.getKursusById() },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DetailStatusKursus(
    kursusUiState: DetailKursusUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (kursusUiState) {
        is DetailKursusUiState.Success -> {
            DetailCard(
                kursus = kursusUiState.kursus,
                modifier = modifier.padding(16.dp)
            )
        }

        is DetailKursusUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailKursusUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Terjadi kesalahan.", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = retryAction) {
                        Text(text = "Coba Lagi")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    kursus: Kursus,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ComponentDetailKrs(
                judul = "ID Kursus",
                isinya = kursus.idKursus,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small)
            )

            ComponentDetailKrs(
                judul = "Nama Kursus",
                isinya = kursus.namaKursus,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small)
            )

            ComponentDetailKrs(
                judul = "Deskripsi",
                isinya = kursus.deskripsi,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small)
            )

            ComponentDetailKrs(
                judul = "Kategori",
                isinya = kursus.kategori,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small)
            )

            ComponentDetailKrs(
                judul = "Harga",
                isinya = formatRupiah(kursus.harga), // Menggunakan format rupiah
                modifier = Modifier.background(MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small)
            )

            ComponentDetailKrs(
                judul = "ID Instruktur",
                isinya = kursus.idInstruktur,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small)
            )
        }
    }
}

// Format Rupiah
fun formatRupiah(value: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(value)
}

@Composable
fun ComponentDetailKrs(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$judul :  ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = isinya,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

