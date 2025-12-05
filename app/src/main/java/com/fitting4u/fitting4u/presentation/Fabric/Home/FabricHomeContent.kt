package com.fitting4u.fitting4u.presentation.Fabric.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.FabricHomeDto
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp

@Composable
fun FabricHomeContent(
    data: FabricHomeDto,
    navController: NavController
) {

    // Full screen layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 🔵 FIXED TOP BAR (always at top)
        FabricTopBar(
            onSearchClick = {
                navController.navigate("fabricExplore?searchMode=true")
            }
            ,
            onCartClick = {
                navController.navigate("cart")
            }
        )

        // 🔽 SCROLLABLE CONTENT BELOW
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {

            item { BannerCarousel(data.banners) }

            item { SectionTitle("Featured Fabrics") }
            item { FeaturedFabricsRow(data.featuredFabrics, navController) }

            item { SectionTitle("Categories") }
            item { CategoryGrid(data.categories, navController) }

            item { SectionTitle("Explore Weaves") }
            item { WeaveRow(data.weaves, navController) }

            item { SectionTitle("Shop by Color") }
            item { ColorRow(data.colors, navController) }

            item {
                ExploreMoreButton { navController.navigate("fabricExplore") }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
