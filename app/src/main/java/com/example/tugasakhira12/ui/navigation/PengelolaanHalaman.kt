package com.example.tugasakhira12.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tugasakhira12.Repository.AppContainer
import com.example.tugasakhira12.ui.HomeScreen.DestinasiHomeScreen
import com.example.tugasakhira12.ui.HomeScreen.DestinasiSplash
import com.example.tugasakhira12.ui.HomeScreen.HomeScreen
import com.example.tugasakhira12.ui.HomeScreen.SplashScreen
import com.example.tugasakhira12.ui.PageKursus.View.DestinasiDetailKursus
import com.example.tugasakhira12.ui.PageKursus.View.DestinasiHomeKursus
import com.example.tugasakhira12.ui.PageKursus.View.DestinasiInsertKursus
import com.example.tugasakhira12.ui.PageKursus.View.DestinasiUpdateKursus
import com.example.tugasakhira12.ui.PageKursus.View.DetailKursusView
import com.example.tugasakhira12.ui.PageKursus.View.InsertKursus
import com.example.tugasakhira12.ui.PageKursus.View.KursusScreen
import com.example.tugasakhira12.ui.PageKursus.View.UpdateKursusView
import com.example.tugasakhira12.ui.PagePendaftaran.View.DestinasiDetailPendaftaran
import com.example.tugasakhira12.ui.PagePendaftaran.View.DestinasiHomePendaftaran
import com.example.tugasakhira12.ui.PagePendaftaran.View.DestinasiInsertPendaftaran
import com.example.tugasakhira12.ui.PagePendaftaran.View.DestinasiUpdatePendaftaran
import com.example.tugasakhira12.ui.PagePendaftaran.View.DetailPendaftaranView
import com.example.tugasakhira12.ui.PagePendaftaran.View.InsertPendaftaran
import com.example.tugasakhira12.ui.PagePendaftaran.View.PendaftaranScreen
import com.example.tugasakhira12.ui.PagePendaftaran.View.UpdatePendaftaranView
import com.example.tugasakhira12.ui.PagesInstruktur.View.DestinasiDetailInstruktur
import com.example.tugasakhira12.ui.PagesInstruktur.View.DestinasiHomeInstruktur
import com.example.tugasakhira12.ui.PagesInstruktur.View.DestinasiInsertInstruktur
import com.example.tugasakhira12.ui.PagesInstruktur.View.DestinasiUpdateInstruktur
import com.example.tugasakhira12.ui.PagesInstruktur.View.DetailInstrukturView
import com.example.tugasakhira12.ui.PagesInstruktur.View.InsertInstruktur
import com.example.tugasakhira12.ui.PagesInstruktur.View.InstrukturScreen
import com.example.tugasakhira12.ui.PagesInstruktur.View.UpdateInstrukturView
import com.example.tugasakhira12.ui.PagesSiswa.View.DestinasiDetail
import com.example.tugasakhira12.ui.PagesSiswa.View.DestinasiHome
import com.example.tugasakhira12.ui.PagesSiswa.View.DestinasiInsertSiswa
import com.example.tugasakhira12.ui.PagesSiswa.View.DestinasiUpdate
import com.example.tugasakhira12.ui.PagesSiswa.View.DetailSiswaView
import com.example.tugasakhira12.ui.PagesSiswa.View.InsertSiswa
import com.example.tugasakhira12.ui.PagesSiswa.View.SiswaScreen
import com.example.tugasakhira12.ui.PagesSiswa.View.UpdateSiswaView

@Composable
fun PengelolaHalaman(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = DestinasiSplash.route,
        modifier = Modifier
    ) {

        //Splash Screen
        composable(DestinasiSplash.route) {
            SplashScreen(navController = navController)
        }

        //HomeScreen
        composable(DestinasiHomeScreen.route) {
            HomeScreen(navSiswa = { navController.navigate(DestinasiHome.route) },
                navInstruktur = { navController.navigate(DestinasiHomeInstruktur.route) },
                navKursus = {navController.navigate(DestinasiHomeKursus.route)},
                navPendaftaran = { navController.navigate(DestinasiHomePendaftaran.route) })
        }

        //Halaman Siswa
        composable(DestinasiHome.route) {
            SiswaScreen(
                navigateBack = {
                    navController.navigate(DestinasiHomeScreen.route) {
                        popUpTo(DestinasiHomeScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onDetailClick = { IdSiswa ->
                    navController.navigate("${DestinasiDetail.route}/$IdSiswa")
                    println(IdSiswa)
                },
                navigateToltemEntry = { navController.navigate(DestinasiInsertSiswa.route) },
                navigateToEdit = { idSiswa ->
                    navController.navigate("${DestinasiUpdate.route}/$idSiswa")},
                navHome = {navController.navigate(DestinasiHomeScreen.route)},
                navSiswa = { navController.navigate(DestinasiHome.route) },
                navInstruktur = { navController.navigate(DestinasiHomeInstruktur.route)  },
                navKursus = {navController.navigate(DestinasiHomeKursus.route)},
                navPendaftaran = { navController.navigate(DestinasiHomePendaftaran.route) })
        }

        composable(DestinasiInsertSiswa.route){
            InsertSiswa(navigateBack = {
                navController.navigate(DestinasiHome.route){
                    popUpTo(DestinasiHome.route){
                        inclusive = true
                    }
                }
            })
        }

        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetail.idSiswa) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val idSiswa = backStackEntry.arguments?.getString(DestinasiDetail.idSiswa)
            idSiswa?.let {
                DetailSiswaView(
                    navigateBack = {
                        navController.navigateUp()
                    },
                    onEditClick = { idSiswa ->
                        navController.navigate("${DestinasiUpdate.route}/$idSiswa")

                    }
                )
            }
        }

        composable(
            route = DestinasiUpdate.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdate.idSiswa){
                type = NavType.StringType
            })
        ){
            UpdateSiswaView(
                navigateBack = {
                    navController.popBackStack()
                },
                onNavigateUp = {
                    navController.navigate(
                        DestinasiUpdate.route
                    ){
                        popUpTo(DestinasiHome.route){
                            inclusive = true
                        }
                    }
                }
            )
        }

        //Halaman Instruktur
        composable(DestinasiHomeInstruktur.route) {
            InstrukturScreen(
                navigateBack = {
                    navController.navigate(DestinasiHomeScreen.route) {
                        popUpTo(DestinasiHomeScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onDetailClick = { idInstruktur ->
                    navController.navigate("${DestinasiDetailInstruktur.route}/$idInstruktur")
                    println(idInstruktur)
                },
                navigateToltemEntry = { navController.navigate(DestinasiInsertInstruktur.route) },
                navigateToEdit = { idSiswa ->
                    navController.navigate("${DestinasiUpdate.route}/$idSiswa")},
                navHome = {navController.navigate(DestinasiHomeScreen.route)},
                navSiswa = { navController.navigate(DestinasiHome.route) },
                navInstruktur = { navController.navigate(DestinasiHomeInstruktur.route)  },
                navKursus = {navController.navigate(DestinasiHomeKursus.route)},
                navPendaftaran = { navController.navigate(DestinasiHomePendaftaran.route) }
            )
        }

        composable(DestinasiInsertInstruktur.route){
            InsertInstruktur(navigateBack = {
                navController.navigate(DestinasiHomeInstruktur.route){
                    popUpTo(DestinasiHomeInstruktur.route){
                        inclusive = true
                    }
                }
            })
        }

        composable(
            route = DestinasiDetailInstruktur.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailInstruktur.idInstruktur) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val idInstruktur = backStackEntry.arguments?.getString(DestinasiDetailInstruktur.idInstruktur)
            idInstruktur?.let {
                DetailInstrukturView(
                    navigateBack = {
                        navController.navigateUp()
                    },
                    onEditClick = { idInstruktur ->
                        navController.navigate("${DestinasiUpdateInstruktur.route}/$idInstruktur")

                    }
                )
            }
        }

        composable(
            route = DestinasiUpdateInstruktur.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdateInstruktur.IdInstruktur){
                type = NavType.StringType
            })
        ){
            UpdateInstrukturView(
                navigateBack = {
                    navController.popBackStack()
                },
                onNavigateUp = {
                    navController.navigate(
                        DestinasiUpdateInstruktur.route
                    ){
                        popUpTo(DestinasiHomeInstruktur.route){
                            inclusive = true
                        }
                    }
                }
            )
        }

        //Halaman Kursus
        composable(DestinasiHomeKursus.route) {
            KursusScreen(
                navigateBack = {
                    navController.navigate(DestinasiHomeScreen.route) {
                        popUpTo(DestinasiHomeScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onDetailClick = { idKursus ->
                    navController.navigate("${DestinasiDetailKursus.route}/$idKursus")
                    println(idKursus)
                },
                navigateToltemEntry = { navController.navigate(DestinasiInsertKursus.route) },
                navigateToEdit = { idKursus ->
                    navController.navigate("${DestinasiUpdateKursus.route}/$idKursus")},
                navHome = {navController.navigate(DestinasiHomeScreen.route)},
                navSiswa = { navController.navigate(DestinasiHome.route) },
                navInstruktur = { navController.navigate(DestinasiHomeInstruktur.route)  },
                navKursus = {navController.navigate(DestinasiHomeKursus.route)},
                navPendaftaran = {navController.navigate(DestinasiHomePendaftaran.route)}
            )
        }

        composable(DestinasiInsertKursus.route){
            InsertKursus(navigateBack = {
                navController.navigate(DestinasiHomeKursus.route){
                    popUpTo(DestinasiHomeKursus.route){
                        inclusive = true
                    }
                }
            })
        }

        composable(
            route = DestinasiDetailKursus.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailKursus.idKursus) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val idKursus = backStackEntry.arguments?.getString(DestinasiDetailKursus.idKursus)
            idKursus?.let {
                DetailKursusView(
                    navigateBack = {
                        navController.navigateUp()
                    },
                    onEditClick = { idKursus ->
                        navController.navigate("${DestinasiUpdateKursus.route}/$idKursus")

                    }
                )
            }
        }

        composable(
            route = DestinasiUpdateKursus.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdateKursus.IdKursus){
                type = NavType.StringType
            })
        ){

            UpdateKursusView(
                navigateBack = {
                    navController.popBackStack()
                },
                onNavigateUp = {
                    navController.navigate(
                        DestinasiUpdateKursus.route
                    ){
                        popUpTo(DestinasiHomeKursus.route){
                            inclusive = true
                        }
                    }
                }
            )
        }

        //Halaman Pendaftaran
        composable(DestinasiHomePendaftaran.route) {
            PendaftaranScreen(
                navigateBack = {
                    navController.navigate(DestinasiHomeScreen.route) {
                        popUpTo(DestinasiHomeScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onDetailClick = { idPendaftaran ->
                    navController.navigate("${DestinasiDetailPendaftaran.route}/$idPendaftaran")
                    println(idPendaftaran)
                },
                navigateToltemEntry = { navController.navigate(DestinasiInsertPendaftaran.route) },
                navigateToEdit = { idKursus ->
                    navController.navigate("${DestinasiUpdateKursus.route}/$idKursus")},
                navHome = {navController.navigate(DestinasiHomeScreen.route)},
                navSiswa = { navController.navigate(DestinasiHome.route) },
                navInstruktur = { navController.navigate(DestinasiHomeInstruktur.route)  },
                navKursus = {navController.navigate(DestinasiHomeKursus.route)},
                navPendaftaran = { navController.navigate(DestinasiHomePendaftaran.route) }
            )
        }

        composable(DestinasiInsertPendaftaran.route){
            InsertPendaftaran(navigateBack = {
                navController.navigate(DestinasiHomePendaftaran.route){
                    popUpTo(DestinasiHomePendaftaran.route){
                        inclusive = true
                    }
                }
            })
        }

        composable(
            route = DestinasiDetailPendaftaran.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailPendaftaran.idPendaftaran) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val idPendaftaran = backStackEntry.arguments?.getString(DestinasiDetailPendaftaran.idPendaftaran)
            idPendaftaran?.let {
                DetailPendaftaranView(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    onEditClick = { idPendaftaran ->
                        navController.navigate("${DestinasiUpdatePendaftaran.route}/$idPendaftaran")

                    }
                )
            }
        }

        composable(
            route = DestinasiUpdatePendaftaran.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdatePendaftaran.IdPendaftaran){
                type = NavType.StringType
            })
        ){
            UpdatePendaftaranView(
                navigateBack = {
                    navController.popBackStack()
                },
                onNavigateUp = {
                    navController.navigate(
                        DestinasiDetailPendaftaran.route
                    ){
                        popUpTo(DestinasiDetailPendaftaran.route){
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}