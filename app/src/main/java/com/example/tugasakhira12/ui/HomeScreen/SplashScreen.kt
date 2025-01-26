package com.example.tugasakhira12.ui.HomeScreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tugasakhira12.R
import com.example.tugasakhira12.ui.navigation.DestinasiNavigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DestinasiSplash : DestinasiNavigation {
    override val route = "splash"
    override val titleRes = "splash screen"
}

@Composable
fun SplashScreen(navController: NavHostController) {
    val circleCount = 10 // Jumlah lingkaran
    val animState = remember { mutableStateListOf<Float>() } // Untuk menyimpan posisi X lingkaran
    val scope = rememberCoroutineScope()

    // Inisialisasi animasi untuk setiap lingkaran
    LaunchedEffect(Unit) {
        repeat(circleCount) { animState.add(0f) } // Semua lingkaran mulai dari posisi X = 0
        for (i in 0 until circleCount) {
            scope.launch {
                animState[i] = 1f // Aktifkan animasi ke kanan untuk lingkaran ini
            }
            delay(200) // Delay antar lingkaran
        }
        delay(2000) // Durasi total animasi
        navController.navigate(DestinasiHomeScreen.route) // Navigasi setelah selesai
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier.size(350.dp)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            animState.forEachIndexed { index, animValue ->
                val positionX by animateFloatAsState(
                    targetValue = animValue,
                    animationSpec = tween(2000) // Durasi animasi per lingkaran (2 detik)
                )
                Box(
                    modifier = Modifier
                        .offset(x = positionX.dp * 30) // Offset ke kanan secara bertahap
                        .size(20.dp)
                        .padding(4.dp)
                        .background(
                            color = colorResource(id = R.color.biruLoad),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}
