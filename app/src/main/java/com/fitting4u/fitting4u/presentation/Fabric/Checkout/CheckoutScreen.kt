package com.fitting4u.fitting4u.presentation.Fabric.Checkout

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitting4u.fitting4u.presentation.Fabric.CartSummaryUiState
import com.fitting4u.fitting4u.presentation.Fabric.CartViewModel
import com.fitting4u.fitting4u.presentation.Fabric.cart.CartContent
import com.fitting4u.fitting4u.presentation.Fabric.cart.EmptyCartView
import com.fitting4u.fitting4u.presentation.Fabric.cart.ShippingSummaryCard
import com.fitting4u.fitting4u.presentation.NavRoutes
import com.fitting4u.fitting4u.presentation.NavRoutes.Checkout
import com.razorpay.Checkout
import org.json.JSONObject


@Composable
fun CheckoutScreen(
    navController: NavController,
    vm: CheckoutViewModel = hiltViewModel(),
    cartVM: CartViewModel = hiltViewModel()
) {
    val ui by vm.ui.collectAsState()
    val cartState by cartVM.cartSummary.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity

    val razorpayLauncher = remember {
        Checkout().apply {
        }
    }
    LaunchedEffect(ui.razorOrder?.id) {
        val id = ui.razorOrder?.id ?: return@LaunchedEffect
        if (id.isNotEmpty()) {

            val options = JSONObject().apply {
                put("name", "Fitting4U")
                put("currency", "INR")
                put("amount", ui.razorOrder?.amount?.times(1000))
                put("order_id", id)
                put("prefill", JSONObject().apply {
                    put("email", "noemail@fitting4u.com")  // dummy but valid format
                    put("contact", ui.address?.phone)
                })
            }

            // OPEN ONLY ONCE
            razorpayLauncher.open(activity, options)
        }
    }

    // Load cart when checkout opens
    LaunchedEffect(Unit) {
        cartVM.loadCartSummary()
        cartVM.refresh()
    }

    when (ui.step) {

        // ------------------------------------------------------------------
        // CART STEP (Now changes behavior if shipping is confirmed)
        // ------------------------------------------------------------------
        CheckoutStep.Cart -> CheckoutCartStep(
            ui = ui,
            cartState = cartState,
            cartVM = cartVM,
            onProceedToShipping = { vm.goTo(CheckoutStep.Shipping) },
            onPay = { amount ->
                vm.createRazorOrder(amount)
            },
            onBack = { navController.popBackStack() }
        )

        // ------------------------------------------------------------------
        // SHIPPING STEP
        // ------------------------------------------------------------------
        CheckoutStep.Shipping ->
            ShippingMethodScreen(
                ui = ui,

                onSelectHome = { vm.setShippingType("HOME") },
                onSelectPickup = { vm.setShippingType("BOUTIQUE") },

                // 🔵 CONTINUE → Save shipping summary → Back to Cart
                onContinue = { summary ->
                    vm.confirmShippingAndReturnToCart(summary)
                },

                onBack = { vm.goTo(CheckoutStep.Cart) },

                onBoutiqueClick = { vm.selectBoutique(it) },
                onSearchPincode = { pin -> vm.searchPincode(pin) },
                onAddressChange = { addr -> vm.updateAddress(addr) }
            )

        // ------------------------------------------------------------------
        // PAYMENT HANDLED INSIDE CART – This is NOT USED ANYMORE
        // ------------------------------------------------------------------
        CheckoutStep.Payment -> {}

        // ------------------------------------------------------------------
        // SUCCESS SCREEN
        // ------------------------------------------------------------------
//        CheckoutStep.Success ->
//            CheckoutSuccessScreen(
//                orderId = ui.successOrderId ?: "",
//                onHome = {
//                    navController.popBackStack(NavRoutes.FabricExplore, false)
//                }
//            )
//

        else -> {}
    }

    // Loading + Error overlays
    if (ui.loading) LoadingOverlay()
    if (ui.error != null)
        ErrorDialog(ui.error!!) { vm.clearError() }
}

@Composable
fun LoadingOverlay() {
    Box(
        Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {

            TextButton(onClick = onDismiss) { Text("OK") }
        },
        title = { Text("Error") },
        text = { Text(message) }
    )
}

@Composable
fun CheckoutCartStep(
    ui: CheckoutUiState,
    cartState: CartSummaryUiState,
    cartVM: CartViewModel,
    onProceedToShipping: () -> Unit,
    onPay: (Double) -> Unit,
    onBack: () -> Unit
) {

    when {
        cartState.loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        cartState.error != null -> ErrorDialog(cartState.error) {
            cartVM.loadCartSummary()
        }

        cartState.data == null -> EmptyCartView {}

        else -> {
            val cartData = cartState.data!!

            Column(Modifier.fillMaxSize()) {

                Text(
                    "Your Cart",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                // 🔵 Shipping summary (only shown AFTER user confirms shipping)
                if (ui.shippingConfirmed && ui.shippingSummary != null) {
                    ShippingSummaryCard(ui.shippingSummary!!)
                    Spacer(Modifier.height(12.dp))
                }

                // 🔵 Pass UI + handlers to CartContent
                CartContent(
                    data = cartData,
                    ui = ui,
                    onIncrease = { id, _ -> cartVM.add(id, 0.25) },
                    onDecrease = { id, _ -> cartVM.decreaseQty(id, 0.25) },
                    onRemove = { id -> cartVM.removeItem(id) },
                    onProceedToShipping = onProceedToShipping,
                    onPay = onPay
                )
            }
        }
    }
}