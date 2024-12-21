package com.nadia.pertemuan9.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nadia.pertemuan9.ui.view.dokter.HomeDokterView
import com.nadia.pertemuan9.ui.view.dokter.InsertDokterView

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AlamatHomeDokter.route
    ) {
        composable(
            route = AlamatHomeDokter.route
        ) {
            HomeDokterView(
                onDetailClick = {},
                onAddDokter = {
                    navController.navigate(AlamatTambahDokter.route)
                },
                navigateLihatJadwal = {
                    navController.navigate(AlamatHomeJadwal.route)
                },
                modifier = modifier
            )
        }

        composable (
            route = AlamatTambahDokter.route
        ) {
            InsertDokterView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
    }
}