package com.example.tugasakhira12.ui.HomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugasakhira12.R
import com.example.tugasakhira12.ui.CustomNavigation.CustomBottomAppBar
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation

object DestinasiHomeScreen : DestinasiNavigation {
    override val route = "homeScreen"
    override val titleRes = "Home screen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
navSiswa: () -> Unit,
navInstruktur: () -> Unit,
navKursus: () -> Unit,
navPendaftaran: () -> Unit
) {

    Scaffold(
        topBar = {
            HeaderSection()
        },
        bottomBar = {
            CustomBottomAppBar(
                onHomeClick = {  },
                onSiswaClick = navSiswa,
                onInstrukturClick = navInstruktur,
                onKursusClick = navKursus,
                onPendaftaranClick = navPendaftaran,
                selectedItem = 0
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = colorResource(id = R.color.white))
        ) {
            // Welcome Text
            Text(
                text = "Selamat Datang",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
            )
            Text(
                text = "ANGGI PUSPITA",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircleMenu(
                onSiswaClick = navSiswa,
                onInstrukturClick = navInstruktur,
                onKursusClick = navKursus,
                onPendaftaranClick = navPendaftaran
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .padding(20.dp)
            .clip(shape = RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.biruNav))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier.size(90.dp)
            )
            Text(
                modifier = Modifier.offset(x=-20.dp),
                text = "COURSE",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                letterSpacing = 2.sp
            )
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription= null
            )
        }
    }

}

@Composable
fun CircleMenu(
    onSiswaClick: () -> Unit,
    onInstrukturClick: () -> Unit,
    onKursusClick: () -> Unit,
    onPendaftaranClick: () -> Unit
) {
    val menuItems = listOf(
        Pair("Siswa", R.drawable.siswa),
        Pair("Instruktur", R.drawable.instruktur),
        Pair("Kursus", R.drawable.kursus),
        Pair("Pendaftaran", R.drawable.pendaftaran)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        menuItems.forEach { (title, iconRes) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        when (title) {
                            "Siswa" -> onSiswaClick()
                            "Instruktur" -> onInstrukturClick()
                            "Kursus" -> onKursusClick()
                            "Pendaftaran" -> onPendaftaranClick()
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = colorResource(id = R.color.biruNav),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = title, fontSize = 14.sp)
            }
        }
    }
}

