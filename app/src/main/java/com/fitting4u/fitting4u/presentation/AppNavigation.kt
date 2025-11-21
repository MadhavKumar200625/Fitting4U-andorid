package com.fitting4u.fitting4u.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fitting4u.fitting4u.presentation.BottomNavigation.BottomNavItem
import com.fitting4u.fitting4u.presentation.BottomNavigation.MainBottomNavigation
import com.fitting4u.fitting4u.presentation.Fabric.CartViewModel
import com.fitting4u.fitting4u.presentation.Fabric.FloatingCartSummaryBar
import com.fitting4u.fitting4u.presentation.Fabric.explore.FabricExploreScreen
import com.fitting4u.fitting4u.presentation.Fabric.Home.FabricHomeScreen
import com.fitting4u.fitting4u.presentation.Fabric.fabric_details.FabricDetailScreen
import com.fitting4u.fitting4u.presentation.Fabric.cart.CartScreen

import com.fitting4u.fitting4u.presentation.config.ConfigViewModel



@Composable
fun AppNavigation(
    configViewModel: ConfigViewModel = hiltViewModel()
) {
    val state = configViewModel.state.observeAsState().value
    val navController = rememberNavController()
    val cartVM: CartViewModel = hiltViewModel()
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
    ) { _ ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.FabricStore.route,
            modifier = Modifier.padding()
        ) {

            composable(BottomNavItem.FabricStore.route) {
                FabricHomeScreen(navController)
            }

            composable(BottomNavItem.Boutiques.route) { BoutiquesScreen() }
            composable(BottomNavItem.DesignNow.route) { DesignNowScreen() }
            composable(BottomNavItem.HomeMeasurement.route) { HomeMeasurementScreen() }
            composable(BottomNavItem.Account.route) { AccountScreen() }


            composable(NavRoutes.FabricExplore) {
                FabricExploreScreen(navController)
            }

            composable(NavRoutes.Cart) { backStackEntry ->
                CartScreen(navController,vm = cartVM)
            }

            composable(
                route = NavRoutes.FabricDetail,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id")!!
                FabricDetailScreen(id, navController = navController, cartVM = cartVM)
            }


        }
    }
    FloatingCartArea(
        navController = navController,
        cartVM = cartVM
    )
}

@Composable
fun FloatingCartArea(
    navController: NavController,
    cartVM: CartViewModel
) {
    val totalQty by cartVM.totalQty.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route ?: ""

    val shouldShow = when (route) {
        NavRoutes.FabricDetail,
        NavRoutes.FabricExplore -> totalQty > 0
        else -> false
    }
    if (shouldShow) {
        FloatingCartSummaryBar(
            totalQty = totalQty,
            onClickGoToCart = { navController.navigate(NavRoutes.Cart) }
        )
    }
}