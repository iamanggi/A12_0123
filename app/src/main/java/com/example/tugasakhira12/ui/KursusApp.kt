package com.example.tugasakhira12.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.example.tugasakhira12.PendKursusApp
import com.example.tugasakhira12.ui.navigation.PengelolaHalaman

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KursusApp(
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    // Get the application instance
    val appContainer = (LocalContext.current.applicationContext as PendKursusApp).mainAppContainer

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ){
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            PengelolaHalaman(appContainer = appContainer)
        }
    }
}