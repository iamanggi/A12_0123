package com.example.tugasakhira12.ui.PagesInstruktur.View

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.example.tugasakhira12.model.Instruktur
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.DetailInstrukturUiState
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.DetailInstrukturViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation


object DestinasiDetailInstruktur : DestinasiNavigation {
    override val route = "idInstruktur_detail"
    override val titleRes = "Detail Instruktur"
    const val idInstruktur = "idInstruktur"
    val routeWithArgs = "$route/{$idInstruktur}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInstrukturView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    detailInstrukturViewModel: DetailInstrukturViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        detailInstrukturViewModel.getInstrukturById() // Memanggil fungsi untuk mengambil data Instruktur
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetailInstruktur.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                onRefresh = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val idInstruktur = (detailInstrukturViewModel.detailInstrukturUiState as? DetailInstrukturUiState.Success)?.instruktur?.idInstruktur
                    if (idInstruktur != null) onEditClick(idInstruktur)
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Instruktur",
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
            DetailStatusInstruktur(
                instUiState = detailInstrukturViewModel.detailInstrukturUiState,
                retryAction = { detailInstrukturViewModel.getInstrukturById() },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DetailStatusInstruktur(
    instUiState: DetailInstrukturUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (instUiState) {
        is DetailInstrukturUiState.Success -> {
            DetailCard(
                instruktur = instUiState.instruktur,
                modifier = modifier.padding(16.dp)
            )
        }

        is DetailInstrukturUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailInstrukturUiState.Error -> {
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
    instruktur: Instruktur,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 1.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Informasi Dasar",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            ComponentDetailInst(
                judul = "ID Instruktur",
                isinya = instruktur.idInstruktur,
                icon = {
                    Icon(imageVector = Icons.Default.Info,
                        contentDescription = "ID Icon")
                }
            )
            ComponentDetailInst(
                judul = "Nama Instruktur",
                isinya = instruktur.namaInstruktur,
                icon = {
                    Icon(imageVector = Icons.Default.Person,
                        contentDescription = "Nama Icon")
                }
            )
            ComponentDetailInst(
                judul = "Email",
                isinya = instruktur.email,
                icon = {
                    Icon(imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon")
                }
            )
            ComponentDetailInst(
                judul = "Nomor Telepon",
                isinya = instruktur.noTelepon,
                icon = {
                    Icon(imageVector = Icons.Default.Phone,
                        contentDescription = "Telepon Icon")
                }
            )

            // Garis Pemisah
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            )

            Text(
                text = "Deskripsi",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = instruktur.deskripsi,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun ComponentDetailInst(
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
