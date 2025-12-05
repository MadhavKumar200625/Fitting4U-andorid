package com.fitting4u.fitting4u.presentation.Fabric.Checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fitting4u.fitting4u.Data.remote.dto.order.boutiques.nearby.Boutique
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.CheckoutAddress
import java.util.*

/**
 * ShippingMethodScreen:
 * - top tabs: Home / Pickup
 * - below shows HomeDeliveryForm or BoutiquePickupScreen
 * - when Continue pressed, it calls onConfirmShipping(map) (map describes preview)
 *
 * NOTE: the caller MUST pass onContinue behavior that calls: vm.confirmShippingAndReturnToCart(summary)
 * so the flow returns to Cart and cart shows the preview.
 */

@Composable
fun ShippingMethodScreen(
    ui: CheckoutUiState,
    onSelectHome: () -> Unit,
    onSelectPickup: () -> Unit,
    // This onContinue should trigger a confirmShippingAndReturnToCart(summary) in VM caller
    onContinue: (Map<String, Any>) -> Unit,
    onBack: () -> Unit,
    onBoutiqueClick: (Boutique) -> Unit,
    onSearchPincode: (String) -> Unit,
    onAddressChange: (CheckoutAddress) -> Unit
) {
    val blue = Color(0xFF003466)
    var selectedTab by remember { mutableStateOf(if (ui.shippingType == "HOME") 0 else 1) }

    // sync
    LaunchedEffect(ui.shippingType) {
        selectedTab = if (ui.shippingType == "HOME") 0 else 1
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Choose Delivery Method", style = MaterialTheme.typography.headlineSmall, color = blue)
        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth()) {
            ShippingTab(text = "Home Delivery", selected = selectedTab == 0, onClick = {
                selectedTab = 0
                onSelectHome()
            }, modifier = Modifier.weight(1f))

            Spacer(Modifier.width(12.dp))

            ShippingTab(text = "Pickup from Boutique", selected = selectedTab == 1, onClick = {
                selectedTab = 1
                onSelectPickup()
            }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        when (selectedTab) {
            0 -> {
                // Home delivery form collects address and passes it to onAddressChange
                HomeDeliveryForm(ui.address) { addr ->
                    onAddressChange(addr)
                }
            }
            1 -> {
                // Boutique selection panel (includes pincode input)
                BoutiquePickupScreenInline(ui, onBoutiqueClick = onBoutiqueClick, onSearchPincode = onSearchPincode)
            }
        }

        Spacer(Modifier.weight(1f))

        // Build summary map and call onContinue with it
        val enabled = selectedTab == 0 || (selectedTab == 1 && ui.selectedBoutique != null)

        Button(
            onClick = {
                // Build summary map
                if (selectedTab == 0) {
                    // address summary
                    val addr = ui.address
                    val summary = mapOf(
                        "type" to "HOME",
                        "address" to (addr ?: CheckoutAddress()) // pass address instance
                    )
                    onContinue(summary)
                } else {
                    // boutique summary
                    val b = ui.selectedBoutique
                    val summary = mapOf(
                        "type" to "BOUTIQUE",
                        "boutique" to (b ?: Boutique())
                    )
                    onContinue(summary)
                }
            },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = blue)
        ) {
            Text("Continue", color = Color.White)
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

@Composable
fun ShippingTab(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val blue = Color(0xFF003466)
    Card(
        modifier = modifier
            .height(46.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) blue else Color.White),
        elevation = CardDefaults.cardElevation(if (selected) 6.dp else 1.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text, color = if (selected) Color.White else blue, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** Home delivery form - simple inputs, fires onAddressChange whenever changed.
 *  For simplicity, this is lightweight; extend validation as needed.
 */
@Composable
fun HomeDeliveryForm(current: CheckoutAddress?, onAddressChange: (CheckoutAddress) -> Unit) {
    // show simple inputs, update address object in-place
    var name by remember { mutableStateOf(current?.name ?: "") }
    var street by remember { mutableStateOf(current?.street ?: "") }
    var city by remember { mutableStateOf(current?.city ?: "") }
    var state by remember { mutableStateOf(current?.state ?: "") }
    var postalCode by remember { mutableStateOf(current?.postalCode ?: "") }
    var landmark by remember { mutableStateOf(current?.landmark ?: "") }
    var phone by remember { mutableStateOf(current?.landmark ?: "") }

    Column {
        Text("Delivery to your saved address or enter a new one", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(value = name, onValueChange = {
            name = it
            onAddressChange(CheckoutAddress(name = name, street = street, landmark = landmark, city = city, state = state, postalCode = postalCode , phone = phone))
        }, label = { Text("Full name") }, singleLine = true, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = phone, onValueChange = {
            phone = it
            onAddressChange(CheckoutAddress(name = name, street = street, landmark = landmark, city = city, state = state, postalCode = postalCode , phone = phone))
        }, label = { Text("Phone Number") }, singleLine = true, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = street, onValueChange = {
            street = it
            onAddressChange(CheckoutAddress(name = name, street = street, landmark = landmark, city = city, state = state, postalCode = postalCode , phone = phone))
        }, label = { Text("Street address") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = city, onValueChange = {
                city = it
                onAddressChange(CheckoutAddress(name = name, street = street, landmark = landmark, city = city, state = state, postalCode = postalCode , phone = phone))
            }, label = { Text("City") }, modifier = Modifier.weight(1f))

            OutlinedTextField(value = state, onValueChange = {
                state = it
                onAddressChange(CheckoutAddress(name = name, street = street, landmark = landmark, city = city, state = state, postalCode = postalCode , phone = phone))
            }, label = { Text("State") }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = postalCode, onValueChange = {
                postalCode = it
                onAddressChange(CheckoutAddress(name = name, street = street, landmark = landmark, city = city, state = state, postalCode = postalCode , phone = phone))
            }, label = { Text("Postal Code") }, modifier = Modifier.weight(1f))

            OutlinedTextField(value = landmark, onValueChange = {
                landmark = it
                onAddressChange(CheckoutAddress(name = name, street = street, landmark = landmark, city = city, state = state, postalCode = postalCode , phone = phone))
            }, label = { Text("Landmark") }, modifier = Modifier.weight(1f))
        }
    }
}

/**
 * Inline boutique panel used inside Shipping screen (so we don't navigate away).
 * Uses pincode input and shows scrollable list.
 */
@Composable
fun BoutiquePickupScreenInline(
    ui: CheckoutUiState,
    onBoutiqueClick: (Boutique) -> Unit,
    onSearchPincode: (String) -> Unit
) {
    Column {
        PincodeInput { pin -> onSearchPincode(pin) }
        Spacer(Modifier.height(10.dp))

        // subtle hint gradient
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.04f)))))

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
            if (ui.nearbyBoutiques.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { Text("No boutiques found", color = Color.Gray) }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                    items(ui.nearbyBoutiques) { b ->
                        BoutiqueCard(b, selected = ui.selectedBoutique?.let { it._id == b._id } ?: false, onClick = { onBoutiqueClick(b) })
                    }
                }
            }
        }
    }
}

@Composable
fun PincodeInput(onSearch: (String) -> Unit) {
    var pin by remember { mutableStateOf("") }
    Column {
        OutlinedTextField(value = pin, onValueChange = { pin = it }, label = { Text("Enter Pincode") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick = { onSearch(pin) }, enabled = pin.length >= 5, modifier = Modifier.fillMaxWidth()) {
            Text("Find Boutiques")
        }
    }
}

@Composable
fun BoutiqueCard(boutique: Boutique, selected: Boolean, onClick: () -> Unit) {
    val blue = Color(0xFF003466)
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) blue.copy(alpha = 0.06f) else Color.White)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = rememberAsyncImagePainter(boutique.businessLogo), contentDescription = null, modifier = Modifier.size(52.dp).clip(CircleShape), contentScale = ContentScale.Crop)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(boutique.title ?: "", color = blue, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(boutique.googleAddress ?: "", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                boutique.distance?.let {
                    Spacer(Modifier.height(4.dp))
                    Text("${"%.1f".format(it/1000)} km away", color = blue.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}