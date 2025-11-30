package com.fitting4u.fitting4u.presentation.boutique.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoutiqueSearchScreen(
    vm: BoutiqueSearchViewModel = hiltViewModel(),
    onClickBoutique: (String) -> Unit
) {
    val ui = vm.ui
    val focus = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var showFilters by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.load(1)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {

        Column(Modifier.fillMaxSize()) {

            SearchBarWithFilters(
                value = ui.search,
                onValueChange = { vm.updateSearch(it) },
                onSearch = { vm.load(1) },
                onFilterClicked = {
                    showFilters = true
                    scope.launch { sheetState.show() }
                }
            )

            BoutiqueList(
                boutiques = ui.boutiques,
                page = ui.page,
                totalPages = ui.totalPages,
                loading = ui.loading,
                onLoadMore = { nextPage ->
                    vm.load(nextPage)
                },
                onClick = onClickBoutique
            )
        }

        if (showFilters) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    showFilters = false
                }
            ) {
                FilterSheet(
                    ui = ui,
                    onApply = { type, price, verified, location ->
                        vm.applyFilters(
                            type = type,
                            priceRange = price,
                            verified = verified,
                            location = location
                        )
                        scope.launch { sheetState.hide() }
                        showFilters = false
                    }
                )
            }
        }
    }
}