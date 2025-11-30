package com.fitting4u.fitting4u.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
import com.fitting4u.fitting4u.presentation.boutique.boutique_detail.BoutiqueDetailScreen
import com.fitting4u.fitting4u.presentation.boutique.home.BoutiqueSearchScreen
import com.fitting4u.fitting4u.presentation.common.UserViewModel
import com.fitting4u.fitting4u.presentation.common.sheet.LoginBottomSheet
import com.fitting4u.fitting4u.presentation.config.ConfigViewModel

@Composable
fun AppNavigation(
    configViewModel: ConfigViewModel = hiltViewModel()
) {
    val state = configViewModel.state.observeAsState().value
    val navController = rememberNavController()

    val cartVM: CartViewModel = hiltViewModel()
    val userVM: UserViewModel = hiltViewModel()

    val phone by userVM.phoneFlow.collectAsState(initial = null)
    val isLoggedIn = phone != null
    val ui by userVM.ui.collectAsState()

    if (state?.navConfig == null) {
        Box(
            Modifier.fillMaxSize(),
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
            startDestination = BottomNavItem.FabricStore.route
        ) {

            composable(BottomNavItem.FabricStore.route) {
                FabricHomeScreen(navController)
            }

            composable(NavRoutes.FabricExplore) {
                FabricExploreScreen(navController)
            }

            composable(
                route = NavRoutes.FabricDetail,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id")!!
                FabricDetailScreen(id, navController, cartVM = cartVM)
            }

            composable(BottomNavItem.Boutiques.route) { BoutiqueSearchScreen(onClickBoutique = {it-> navController.navigate("boutiqueDetail/${it}")}) }
            composable(BottomNavItem.DesignNow.route) { DesignNowScreen() }
            composable(BottomNavItem.HomeMeasurement.route) { HomeMeasurementScreen() }
            composable(BottomNavItem.Account.route) { AccountScreen() }

            composable(
                route = NavRoutes.BoutiqueDetail,
                arguments = listOf(navArgument("slug") { type = NavType.StringType })
            ) { entry ->

                val slug = entry.arguments?.getString("slug")!!
                BoutiqueDetailScreen(
                    id = slug,
                    onNavigateToBoutiqueSearch = {

                    }
                )
            }

            // ⭐ SIMPLE LOGIN FLOW ⭐
            composable(NavRoutes.Cart) {
                if (isLoggedIn) {
                    CartScreen(navController, cartVM)
                } else {
                    userVM.openLoginSheetFor {
                        navController.navigate(NavRoutes.Cart)
                    }
                }
            }
        }
    }

    // Floating cart stays untouched
    FloatingCartArea(
        navController = navController,
        cartVM = cartVM
    )

    // ⭐ NEW SIMPLE SHEET HANDLER ⭐
    if (ui.showLoginSheet) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .pointerInput(Unit) {}
        ) {
            LoginBottomSheet(
                vm = userVM,
                onSuccess = {
                    userVM.closeLoginSheet()
                    userVM.resumePendingAction()
                },
                onDismiss = {
                    userVM.closeLoginSheet()
                }
            )
        }
    }
}


@Composable
fun CheckoutScreen(x0: NavHostController) {
    Text("this is checkout screen")
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