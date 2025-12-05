// CartScreenWithPayment.kt
package com.fitting4u.fitting4u.presentation.Fabric.cart

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitting4u.fitting4u.presentation.Fabric.CartViewModel
import com.fitting4u.fitting4u.presentation.Fabric.CartSummaryUiState
import com.fitting4u.fitting4u.presentation.Fabric.Checkout.CheckoutUiState
import com.fitting4u.fitting4u.presentation.Fabric.Checkout.CheckoutViewModel
import com.fitting4u.fitting4u.presentation.NavRoutes
import com.razorpay.Checkout
import org.json.JSONObject
import com.fitting4u.fitting4u.Data.remote.request_model.order.verify.VerifyRequestModel
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.ConfirmOrderRequest
import kotlinx.coroutines.launch

//@Composable
//fun CartScreen(
//    navController: NavController,
//    vm: CartViewModel = hiltViewModel(),
//    checkoutVm: CheckoutViewModel = hiltViewModel()
//) {
//    val state by vm.cartSummary.collectAsState()
//    val totalQty by vm.totalQty.collectAsState()
//    val ui by checkoutVm.ui.collectAsState()
//
//    val context = LocalContext.current
//    val activity = remember { context as Activity }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    // load on enter
//    LaunchedEffect(Unit) {
//        vm.loadCartSummary()
//        vm.refresh()
//    }
//
//    Box(modifier = Modifier.fillMaxSize().padding(bottom = 90.dp , top = 20.dp)) {
//        when {
//            state.loading -> {
//                // Center loading
//                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator()
//                }
//            }
//
//            state.error != null -> {
//                // Error view
//                ErrorView(error = state.error ?: "Unknown") {
//                    vm.loadCartSummary()
//                }
//            }
//
//            state.data == null -> {
//                EmptyCartView {
//                    // navigate to explore/home
//                    navController.navigate(NavRoutes.FabricExplore)
//                }
//            }
//
//            else -> {
//                Column(Modifier.fillMaxSize()) {
//
//                    // SHIPPING SUMMARY CARD (Option A: above cart items)
//                    if (ui.shippingConfirmed && ui.shippingSummary != null) {
//                        ShippingSummaryCard(ui.shippingSummary!!)
//                        Spacer(Modifier.height(12.dp))
//                    }
//
//                    // show cart content
//                    CartContent(
//                        data = state.data!!,
//                        onIncrease = { id, deltaQty -> vm.add(id, deltaQty) },
//                        onDecrease = { id, minus -> vm.decreaseQty(id, minus) },
//                        onRemove = { id -> vm.removeItem(id) },
//                        onCheckout = {
//                            // Navigate to shipping screen if user taps original checkout button
//                            navController.navigate(NavRoutes.Checkout)
//                        }
//                    )
//
//                    Spacer(Modifier.weight(1f))
//
//                    // Pay button (grandTotal from the GetCart DTO)
//                    val grandTotal = state.data!!.bill.total // <-- you told me the field is grandtotal; adjust if different
//                    // In your earlier message you said 'grandtotal' — if the DTO uses 'total' use that; change here if needed.
//
//                    Button(
//                        onClick = {
//                            // Begin flow: create Razor order then open checkout
//                            checkoutVm.createRazorOrder(grandTotal)
//                            // We observe razorOrder change below and will open Razorpay when available.
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text("Pay ₹${"%.2f".format(grandTotal)}")
//                    }
//                }
//
//                // Observe razorOrder creation and open Razorpay when available
//                LaunchedEffect(ui.razorOrder) {
//                    val order = ui.razorOrder
//                    if (order == null) return@LaunchedEffect
//
//                    // Build options JSON required by Razorpay SDK
//                    try {
//                        val options = JSONObject()
//                        // server should provide order.id and amount (in paise usually)
//                        // Ensure your backend returned the fields: id, amount (in smallest currency unit)
//                        options.put("name", "Fitting4U")
//                        options.put("description", "Fabric Order")
//                        // order.amount expected in paise (Integer) - confirm with your backend
//                        options.put("amount", order.amount ?: 0) // amount in paise
//                        options.put("currency", order.currency ?: "INR")
//                        options.put("order_id", order.id ?: "")
//
//                        // prefill can be provided if you keep user info
//                        val prefill = JSONObject()
//                        // optional: set phone / email if you have them (not included in ui here)
//                        // prefill.put("contact", userPhone)
//                        options.put("prefill", prefill)
//
//                        // Open Razorpay Checkout (native)
//                        val co = Checkout()
//                        // The key id is usually not required if server returns an order and your manifest has it,
//                        // but you can set it here if needed:
//                        // co.setKeyID("<YOUR_KEY_ID_FROM_SERVER_OR_ENV>")
//
//                        // IMPORTANT:
//                        // Razorpay will callback into the Activity implementing PaymentResultListener:
//                        // - onPaymentSuccess(razorpay_payment_id, paymentData)
//                        // - onPaymentError(code, description)
//                        //
//                        // You must implement PaymentResultListener in the hosting Activity (e.g., MainActivity)
//                        // and from there pass the result to checkoutVm.verifyPayment(...) by building
//                        // VerifyRequestModel with razorpay_payment_id, razorpay_order_id and razorpay_signature
//                        //
//                        // Example flow (in your Activity):
//                        // override fun onPaymentSuccess(razorpayPaymentId: String) {
//                        //   val req = VerifyRequestModel(razorpay_order_id = order.id, razorpay_payment_id = razorpayPaymentId, razorpay_signature = "<signature from callback>")
//                        //   // usually signature is provided by Razorpay on callback
//                        //   // call viewModel.verifyPayment(req) and then viewModel.confirmOrder(...)
//                        // }
//                        //
//                        // Because the SDK requires the Activity to implement PaymentResultListener, ensure you wire it.
//
//                        co.open(activity, options)
//                    } catch (e: Exception) {
//                        Log.e("CartScreen", "Error opening Razorpay: ${e.message}")
//                        checkoutVm.clearError()
//                        // Optionally set error state in vm
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
private fun ErrorView(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Error: $error", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun ShippingSummaryCard(summary: Map<String, Any>) {
    val blue = Color(0xFF003466)
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), colors = CardDefaults.cardColors(containerColor = blue.copy(alpha = 0.06f))) {
        Column(Modifier.padding(12.dp)) {
            val type = summary["type"] as? String
            if (type == "HOME") {
                Text("Delivery: Home Address", color = blue, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                val addr = summary["address"]
                // addr is CheckoutAddress; show short summary
                if (addr is com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.CheckoutAddress) {
                    Text(addr.name ?: "", style = MaterialTheme.typography.bodyMedium)
                    addr.street?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
                    Text("${addr.city ?: ""}, ${addr.state ?: ""} ${addr.postalCode ?: ""}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                } else {
                    Text(addr?.toString() ?: "", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            } else {
                Text("Delivery: Pickup Boutique", color = blue, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                val b = summary["boutique"]
                if (b is com.fitting4u.fitting4u.Data.remote.dto.order.boutiques.nearby.Boutique) {
                    Text(b.title ?: "", style = MaterialTheme.typography.bodyMedium)
                    Text(b.googleAddress ?: "", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    b.distance?.let {
                        Text("${"%.1f".format(it/1000)} km away", style = MaterialTheme.typography.bodySmall, color = blue.copy(alpha = 0.85f))
                    }
                } else {
                    Text(b?.toString() ?: "", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}