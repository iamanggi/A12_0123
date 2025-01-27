package com.example.tugasakhira12.ui.PagePendaftaran.View

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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.example.tugasakhira12.model.Pendaftaran
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.DetailPendUiState
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.DetailPendViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation

object DestinasiDetailPendaftaran : DestinasiNavigation {
    override val route = "idPendaftaran_detail"
    override val titleRes = "Detail Pendaftaran"
    const val idPendaftaran = "idPendaftaran"
    val routeWithArgs = "$route/{$idPendaftaran}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPendaftaranView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    detailPendViewModel: DetailPendViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        detailPendViewModel.getPendaftaranById() // Memanggil fungsi untuk mengambil data pendaftaran
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetailPendaftaran.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                onRefresh = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val idSiswa = (detailPendViewModel.detailPendUiState as? DetailPendUiState.Success)?.pendaftaran?.idPendaftaran
                    if (idSiswa != null) onEditClick(idSiswa)
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Siswa",
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
            DetailStatus(
                pndftranUiState = detailPendViewModel.detailPendUiState,
                retryAction = { detailPendViewModel.getPendaftaranById() },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DetailStatus(
    pndftranUiState: DetailPendUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (pndftranUiState) {
        is DetailPendUiState.Success -> {
            DetailCard(
                pendaftaran = pndftranUiState.pendaftaran,
                modifier = modifier.padding(16.dp)
            )
        }

        is DetailPendUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailPendUiState.Error -> {
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
    pendaftaran: Pendaftaran,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 1.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ComponentDetailPend(
                judul = "Id Pendaftaran",
                isinya = pendaftaran.idPendaftaran,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            ComponentDetailPend(
                judul = "Nama Siswa",
                isinya = pendaftaran.namaSiswa.toString(),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            ComponentDetailPend(
                judul = "Nama Kursus",
                isinya = pendaftaran.namaKursus.toString(),
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            ComponentDetailPend(
                judul = "Tanggal Pendaftaran",
                isinya = pendaftaran.tglPendaftaran,
                icon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            ComponentDetailPend(
                judul = "Status",
                isinya = pendaftaran.status,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}


@Composable
fun ComponentDetailPend(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
    icon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (icon != null) {
            icon()
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = judul,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.padding(top = 5.dp))
            Text(
                text = isinya,
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        }
    }
}

