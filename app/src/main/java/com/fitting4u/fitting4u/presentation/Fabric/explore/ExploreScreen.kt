package com.fitting4u.fitting4u.presentation.Fabric.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/* -------------------------------------------------------------------------- */
/*                              MAIN SCREEN                                   */
/* -------------------------------------------------------------------------- */

@Composable
fun FabricExploreScreen(
    navController: NavController,
    vm: FabricExploreViewModel = hiltViewModel(),

) {
    val state by vm.state.collectAsState()
    val gridState = rememberLazyGridState()
    val coroutine = rememberCoroutineScope()

    var localQuery by rememberSaveable { mutableStateOf("") }

    var sheetVisible by remember { mutableStateOf(false) }

    // this holds what user has applied (real applied filters)
    val appliedFilters = remember { mutableStateMapOf<String, String?>() }

    /* ---------------- Search Debounce ---------------- */
    LaunchedEffect(localQuery) {
        val text = localQuery
        delay(300)
        if (text == localQuery) {
            vm.setSearch(if (text.isBlank()) null else text)
        }
    }

    /* ---------------- Pagination ---------------- */
    LaunchedEffect(gridState, state.isLoading, state.page, state.totalPages) {
        snapshotFlow { gridState.layoutInfo }
            .map { it.visibleItemsInfo.lastOrNull()?.index ?: -1 }
            .distinctUntilChanged()
            .collectLatest { lastVisible ->
                val total = gridState.layoutInfo.totalItemsCount
                if (lastVisible >= total - 6 && !state.isLoading && state.page < state.totalPages) {
                    vm.loadNextPage()
                }
            }
    }

    /* ------------------- UI ---------------------- */

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .systemBarsPadding()
    ) {
        Column(Modifier.fillMaxSize()) {

            Spacer(Modifier.height(18.dp))

            SearchBar(
                query = localQuery,
                onQueryChange = { localQuery = it },
                onFiltersClick = { sheetVisible = true },
                onClear = { localQuery = ""; vm.setSearch(null) }
            )

            if (appliedFilters.isNotEmpty()) {
                AppliedFiltersRow(
                    applied = appliedFilters,
                    onRemove = { key ->
                        appliedFilters.remove(key)
                        vm.updateFilter(key, null)
                    }
                )
            }

            if (state.isLoading && state.fabrics.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF003466))
                }
            } else {
                FabricList(
                    state = state,
                    gridState = gridState,
                    onItemClick = {navController.navigate("fabricDetail/${it.slug}")}
                )
            }
        }

        /* ------------------ FILTER SHEET ------------------ */
        FilterBottomSheet(
            visible = sheetVisible,
            filters = state.filters,
            appliedFilters = appliedFilters,
            onDismissRequest = { sheetVisible = false },
            onApply = { newMap ->
                coroutine.launch {
                    appliedFilters.clear()
                    appliedFilters.putAll(newMap)

                    // update VM filters
                    newMap.forEach { (key, value) ->
                        vm.updateFilter(key, value)
                    }

                    vm.setSearch(if (localQuery.isBlank()) null else localQuery)

                    sheetVisible = false
                }
            },
            onClearAll = {
                coroutine.launch {
                    appliedFilters.clear()
                    vm.refresh()
                    sheetVisible = false
                }
            }
        )
    }
}

/* -------------------------------------------------------------------------- */
/*                           SEARCH BAR                                       */
/* -------------------------------------------------------------------------- */

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFiltersClick: () -> Unit,
    onClear: () -> Unit,
    onSearchClick: (() -> Unit)? = null
) {
    val blue = Color(0xFF003466)
    val inputBg = Color(0xFFF9FBFF)

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // ✨ Glow effect like boutique search bar
    val glow by animateDpAsState(
        targetValue = if (isFocused) 14.dp else 0.dp,
        animationSpec = tween(350, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp , vertical = 8.dp)
            .shadow(
                glow,
                RoundedCornerShape(18.dp),
                ambientColor = blue
            )
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ⬅️ SEARCH TEXTFIELD (same UI)
                TextField(
                    value = query,
                    onValueChange = { onQueryChange(it) },
                    placeholder = {
                        Text(
                            "Search fabrics, prints, colors…",
                            color = Color.Gray.copy(alpha = 0.7f)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .onFocusChanged { isFocused = it.isFocused },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = blue
                        )
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            // 🎛 FILTER BUTTON
                            IconButton(
                                onClick = {
                                    focusManager.clearFocus()
                                    onFiltersClick()
                                }
                            ) {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = "Filters",
                                    tint = blue
                                )
                            }

                            // ❌ CLEAR BUTTON
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { onClear() }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        cursorColor = blue,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                // 🔍 SEARCH BUTTON (same as boutique)
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onSearchClick?.invoke()
                    },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blue
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search Now",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Search", color = Color.White)
                }
            }
        }
    }
}