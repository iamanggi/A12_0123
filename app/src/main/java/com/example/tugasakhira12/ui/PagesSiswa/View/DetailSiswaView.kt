package com.example.tugasakhira12.ui.PagesSiswa.View

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
import com.example.tugasakhira12.model.Siswa
import com.example.tugasakhira12.ui.CustomNavigation.CostumeTopAppBar
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.DetailSiswaUiState
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.DetailSiswaViewModel
import com.example.tugasakhira12.ui.PenyediaViewModel
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation


object DestinasiDetail : DestinasiNavigation {
    override val route = "idSiswa_detail"
    override val titleRes = "Detail Siswa"
    const val idSiswa = "idSiswa"
    val routeWithArgs = "$route/{$idSiswa}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSiswaView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    detailSiswaViewModel: DetailSiswaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        detailSiswaViewModel.getSiswaById() // Memanggil fungsi untuk mengambil data Siswa
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetail.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                onRefresh = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val idSiswa = (detailSiswaViewModel.detailSiswaUiState as? DetailSiswaUiState.Success)?.siswa?.idSiswa
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
                mhsUiState = detailSiswaViewModel.detailSiswaUiState,
                retryAction = { detailSiswaViewModel.getSiswaById() },
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
    mhsUiState: DetailSiswaUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (mhsUiState) {
        is DetailSiswaUiState.Success -> {
            DetailCard(
                siswa = mhsUiState.siswa,
                modifier = modifier.padding(16.dp)
            )
        }

        is DetailSiswaUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailSiswaUiState.Error -> {
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
    siswa: Siswa,
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
            ComponentDetailMhs(
                judul = "ID Siswa",
                isinya = siswa.idSiswa,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "ID Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            ComponentDetailMhs(
                judul = "Nama Siswa",
                isinya = siswa.namaSiswa,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nama Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            ComponentDetailMhs(
                judul = "Email",
                isinya = siswa.email,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            ComponentDetailMhs(
                judul = "Nomor Telepon",
                isinya = siswa.nomorTlp,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Telepon Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}


@Composable
fun ComponentDetailMhs(
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

