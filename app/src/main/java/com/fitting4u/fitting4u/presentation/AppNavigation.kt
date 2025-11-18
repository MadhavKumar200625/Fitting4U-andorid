package com.fitting4u.fitting4u.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fitting4u.fitting4u.presentation.BottomNavigation.BottomNavItem
import com.fitting4u.fitting4u.presentation.BottomNavigation.MainBottomNavigation
import com.fitting4u.fitting4u.presentation.Fabric.explore.FabricExploreScreen
import com.fitting4u.fitting4u.presentation.Fabric.Home.FabricHomeScreen

import com.fitting4u.fitting4u.presentation.config.ConfigViewModel



@Composable
fun AppNavigation(
    configViewModel: ConfigViewModel = hiltViewModel()
) {
    val state = configViewModel.state.observeAsState().value
    val navController = rememberNavController()

    // Wait for config before drawing UI
    if (state?.navConfig == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        bottomBar = {
            MainBottomNavigation(
                navConfig = state.navConfig,
                navController = navController
            )
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.FabricStore.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(BottomNavItem.FabricStore.route) {
                FabricHomeScreen(navController)
            }

            composable(BottomNavItem.Boutiques.route) { BoutiquesScreen() }
            composable(BottomNavItem.DesignNow.route) { DesignNowScreen() }
            composable(BottomNavItem.HomeMeasurement.route) { HomeMeasurementScreen() }
            composable(BottomNavItem.Account.route) { AccountScreen() }


            composable(NavRoutes.FabricExplore) {
                FabricExploreScreen()
            }

//            composable(
//                route = NavRoutes.FabricDetail,
//                arguments = listOf(
//                    navArgument("slug") { type = NavType.StringType }
//                )
//            ) { backStackEntry ->
//                val slug = backStackEntry.arguments?.getString("slug") ?: ""
//                FabricDetailScreen(slug = slug)
//            }
        }
    }
}