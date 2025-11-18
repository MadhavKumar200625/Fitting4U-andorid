package com.fitting4u.fitting4u.presentation.BottomNavigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.fitting4u.fitting4u.domain.models.Config

@Composable
fun MainBottomNavigation(navConfig: Config, navController: NavController) {

    val items = mutableListOf<BottomNavItem>()

    if (navConfig.fabricStore) items += BottomNavItem.FabricStore
    if (navConfig.boutiques) items += BottomNavItem.Boutiques
    if (navConfig.designNow) items += BottomNavItem.DesignNow
    if (navConfig.homeMeasurement) items += BottomNavItem.HomeMeasurement

    items += BottomNavItem.Account // always

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}