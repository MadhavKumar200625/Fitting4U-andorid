package com.fitting4u.fitting4u.presentation.Fabric.explore

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fitting4u.fitting4u.Data.remote.dto.fabric.explore.Fabric
import kotlinx.coroutines.flow.collectLatest

/* ------------------------------------------------------
   MAIN SCREEN
-------------------------------------------------------*/
@Composable
fun FabricExploreScreen(
    vm: FabricExploreViewModel = hiltViewModel(),
    onOpenFilters: () -> Unit = {}
) {
    val state by vm.state.collectAsState()

    val listState = rememberLazyListState()
    // Infinite Scroll Pagination
    LaunchedEffect(listState, state.isLoading, state.page, state.totalPages) {
        snapshotFlow { listState.layoutInfo }
            .collectLatest { info ->
                val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: return@collectLatest

                if (
                    lastVisible >= info.totalItemsCount - 4 &&  // near the bottom
                    !state.isLoading &&
                    state.page < state.totalPages
                ) {
                    println("🟡 PAGINATION → Loading page ${state.page + 1}")
                    vm.loadNextPage()
                }
            }
    }
    val selectedFilters = remember { mutableStateMapOf<String, String?>() }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {

        Spacer(Modifier.height(18.dp))

        ExploreSearchBar(
            query = "",
            onQueryChange = {},
            onSearch = { vm.setSearch(it) },
            onOpenFilters = onOpenFilters
        )

        if (state.filtersLoaded) {
            FullFilterBlock(
                filters = state.filters,
                selectedFilters = selectedFilters,
                onSelect = { key, value ->
                    val current = selectedFilters[key]

                    if (current == value) {
                        selectedFilters.remove(key)
                        vm.updateFilter(key, null)
                    } else {
                        selectedFilters[key] = value
                        vm.updateFilter(key, value)
                    }
                },
                onClearAll = {
                    selectedFilters.clear()
                    vm.refresh()
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
                listState = listState,
                onItemClick = { println("Clicked → ${it.slug}") }
            )
        }
    }
}

/* ------------------------------------------------------
   SEARCH BAR
-------------------------------------------------------*/
@Composable
fun ExploreSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onOpenFilters: () -> Unit
) {
    var localQuery by remember { mutableStateOf(query) }
    val primaryBlue = Color(0xFF003466)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE8EDF3), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(Icons.Default.Search, null, tint = primaryBlue)

            Spacer(Modifier.width(10.dp))

            TextField(
                value = localQuery,
                onValueChange = {
                    localQuery = it
                    onQueryChange(it)
                },
                placeholder = { Text("Search fabrics, prints, colors...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = primaryBlue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )

            Text(
                "Go",
                Modifier
                    .clickable { onSearch(localQuery) }
                    .padding(10.dp),
                color = primaryBlue,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}

/* ------------------------------------------------------
   COLLAPSABLE FULL FILTER BLOCK
-------------------------------------------------------*/
@Composable
fun FullFilterBlock(
    filters: Map<String, List<String>>,
    selectedFilters: MutableMap<String, String?>,
    onSelect: (String, String) -> Unit,
    onClearAll: () -> Unit
) {
    var filtersExpanded by remember { mutableStateOf(true) }

    Column(Modifier.padding(horizontal = 16.dp)) {

        // TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { filtersExpanded = !filtersExpanded }
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Filters",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003466),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (filtersExpanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF003466)
            )
        }

        // CLEAR ALL
        if (filtersExpanded) {
            Text(
                "Clear All",
                color = Color(0xFF003466),
                modifier = Modifier
                    .clickable { onClearAll() }
                    .padding(bottom = 10.dp)
            )
        }

        AnimatedVisibility(
            visible = filtersExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                filters.forEach { (key, list) ->
                    CollapsableFilterGroup(
                        title = key,
                        values = list,
                        selected = selectedFilters[key],
                        onSelect = { onSelect(key, it) },
                        onClear = { onSelect(key, "") }
                    )
                }
            }
        }
    }
}

/* ------------------------------------------------------
   INDIVIDUAL COLLAPSABLE FILTER GROUP
-------------------------------------------------------*/
@Composable
fun CollapsableFilterGroup(
    title: String,
    values: List<String>,
    selected: String?,
    onSelect: (String) -> Unit,
    onClear: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {

        Row(
            Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.Medium,
                color = Color(0xFF003466),
                modifier = Modifier.weight(1f)
            )

            Text(
                if (selected != null) "Clear" else "",
                color = Color(0xFF003466),
                modifier = Modifier.clickable { onClear() }
            )

            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF003466)
            )
        }

        AnimatedVisibility(expanded) {
            val scroll = rememberScrollState()

            Row(
                Modifier
                    .padding(bottom = 6.dp)
                    .horizontalScroll(scroll),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                values.forEach { value ->
                    val isSelected = selected == value

                    FilterChip(
                        label = value,
                        selected = isSelected,
                        onClick = { onSelect(value) }
                    )
                }
            }
        }
    }
}

/* ------------------------------------------------------
   CHIP
-------------------------------------------------------*/
@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Color(0xFF003466) else Color.White
    val content = if (selected) Color.White else Color(0xFF003466)

    Box(
        Modifier
            .background(bg, RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFE6ECF3), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(label, color = content)
    }
}

/* ------------------------------------------------------
   FABRIC LIST
-------------------------------------------------------*/
@Composable
fun FabricList(
    state: ExploreUiState,
    listState: LazyListState,
    onItemClick: (Fabric) -> Unit
) {
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(state.fabrics) { item ->
            FabricCard(item, onClick = { onItemClick(item) })
        }

        item {
            if (state.isLoading) {
                Box(Modifier.fillMaxWidth(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Spacer(Modifier.height(30.dp))
            }
        }
    }
}

/* ------------------------------------------------------
   FABRIC CARD
-------------------------------------------------------*/
@Composable
fun FabricCard(item: Fabric, onClick: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() }
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row {

            val img = item.images.firstOrNull().takeIf { it!!.isNotBlank() }

            if (img != null) {
                AsyncImage(
                    model = img,
                    contentDescription = item.name,
                    modifier = Modifier
                        .width(140.dp)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    Modifier
                        .width(140.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFF0F4F8)),
                    Alignment.Center
                ) {
                    Text("No Image", color = Color.Gray)
                }
            }

            Column(
                Modifier
                    .padding(12.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(item.name, fontWeight = FontWeight.Bold, color = Color(0xFF003466))
                    Spacer(Modifier.height(4.dp))
                    Text(item.material, color = Color.Gray)
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("₹${item.customerPrice}", fontWeight = FontWeight.Bold)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107))
                        Spacer(Modifier.width(4.dp))
                        Text(item.avgStars.toString())
                    }
                }
            }
        }
    }
}