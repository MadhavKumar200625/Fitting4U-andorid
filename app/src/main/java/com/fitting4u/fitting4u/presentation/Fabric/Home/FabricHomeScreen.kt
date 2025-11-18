package com.fitting4u.fitting4u.presentation.Fabric.Home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitting4u.fitting4u.presentation.Fabric.FabricHomeViewModel

@Composable
fun FabricHomeScreen(
    navController: NavController,
    viewModel: FabricHomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.observeAsState().value

    if (state?.isLoading == true || state?.data == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    FabricHomeContent(
        data = state.data!!,
        navController = navController
    )
}