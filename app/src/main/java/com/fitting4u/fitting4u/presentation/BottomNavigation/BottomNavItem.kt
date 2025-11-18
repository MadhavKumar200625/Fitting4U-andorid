package com.fitting4u.fitting4u.presentation.BottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {

    object FabricStore : BottomNavItem("fabrics", "Fabrics", Icons.Default.ShoppingBag)
    object Boutiques : BottomNavItem("boutiques", "Boutiques", Icons.Default.Store)
    object DesignNow : BottomNavItem("design", "Design", Icons.Default.Brush)
    object HomeMeasurement : BottomNavItem("homeMeasurement", "Measurement", Icons.Default.Straighten)
    object Account : BottomNavItem("account", "Account", Icons.Default.Person)
}