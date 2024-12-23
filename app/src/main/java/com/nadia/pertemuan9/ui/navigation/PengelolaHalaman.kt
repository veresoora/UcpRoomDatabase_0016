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
import com.nadia.pertemuan9.ui.view.jadwal.DetailJadwalView
import com.nadia.pertemuan9.ui.view.jadwal.HomejadwalView
import com.nadia.pertemuan9.ui.view.jadwal.InsertJadwalView
import com.nadia.pertemuan9.ui.view.jadwal.UpdateJadwalView

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

        composable(
            route = AlamatHomeJadwal.route
        ) {
            HomejadwalView(
                onDetailClick = { idjadwal ->
                    navController.navigate("${AlamatDetailJadwal.route}/$idjadwal")
                    println("PengelolaHalaman: idjadwal = $idjadwal")
                },
                onAddJadwal = {
                    navController.navigate(AlamatTambahJadwal.route)
                },
                navigateLihatDokter = {
                    navController.navigate(AlamatHomeDokter.route)
                },
                modifier = modifier
            )
        }

        composable(
            route = AlamatTambahJadwal.route
        ) {
            InsertJadwalView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        composable (
            AlamatDetailJadwal.routeWithArg,
            arguments = listOf(
                navArgument(AlamatDetailJadwal.IDJWL) {
                    type = NavType.IntType
                }
            )
        ){
            val idjadwal = it.arguments?.getInt(AlamatDetailJadwal.IDJWL)

            idjadwal?.let { idjadwal ->
                DetailJadwalView(
                    onBack = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("${AlamatEditJadwal.route}/$idjadwal")
                    },
                    modifier = modifier,
                    onDeleteClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            AlamatEditJadwal.routeWithArg,
            arguments = listOf(
                navArgument (AlamatEditJadwal.IDJWL) {
                    type = NavType.IntType
                }
            )
        ) {
            UpdateJadwalView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier,
            )
        }
    }
}