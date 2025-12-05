package com.fitting4u.fitting4u.presentation.Fabric.Checkout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.CheckoutAddress

@Composable
fun HomeDeliveryForm(
    ui: CheckoutUiState,
    onAddressChange: (CheckoutAddress) -> Unit
) {
    val blue = Color(0xFF003466)

    var fullName by remember { mutableStateOf(ui.address?.name ?: "") }
    var street by remember { mutableStateOf(ui.address?.street ?: "") }
    var city by remember { mutableStateOf(ui.address?.city ?: "") }
    var state by remember { mutableStateOf(ui.address?.state ?: "") }
    var postal by remember { mutableStateOf(ui.address?.postalCode ?: "") }
    var country by remember { mutableStateOf(ui.address?.country ?: "India") }
    var district by remember { mutableStateOf(ui.address?.district ?: "") }
    var landmark by remember { mutableStateOf(ui.address?.landmark ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {

        Text(
            "Home Delivery Address",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = blue
        )

        Spacer(Modifier.height(12.dp))

        // FULL NAME
        LabeledTextField(
            label = "Full Name",
            value = fullName,
            onValueChange = {
                fullName = it
                onAddressChange(
                    CheckoutAddress(
                        name = fullName,
                        street = street,
                        city = city,
                        state = state,
                        postalCode = postal,
                        country = country,
                        district = district,
                        landmark = landmark
                    )
                )
            }
        )

        Spacer(Modifier.height(12.dp))

        // STREET
        LabeledTextField(
            label = "Street Address",
            placeholder = "House No, Street, Locality",
            value = street,
            onValueChange = {
                street = it
                onAddressChange(
                    ui.address?.copy(street = street)
                        ?: CheckoutAddress(street = street)
                )
            }
        )

        Spacer(Modifier.height(12.dp))

        // CITY + STATE
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            LabeledTextField(
                modifier = Modifier.weight(1f),
                label = "City",
                value = city,
                onValueChange = {
                    city = it
                    onAddressChange(ui.address?.copy(city = city)
                        ?: CheckoutAddress(city = city))
                }
            )

            LabeledTextField(
                modifier = Modifier.weight(1f),
                label = "State",
                value = state,
                onValueChange = {
                    state = it
                    onAddressChange(ui.address?.copy(state = state)
                        ?: CheckoutAddress(state = state))
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        // DISTRICT + LANDMARK
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            LabeledTextField(
                modifier = Modifier.weight(1f),
                label = "Landmark",
                value = landmark,
                onValueChange = {
                    landmark = it
                    onAddressChange(ui.address?.copy(landmark = landmark)
                        ?: CheckoutAddress(landmark = landmark))
                }
            )

            LabeledTextField(
                modifier = Modifier.weight(1f),
                label = "District",
                value = district,
                onValueChange = {
                    district = it
                    onAddressChange(ui.address?.copy(district = district)
                        ?: CheckoutAddress(district = district))
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        // POSTAL + COUNTRY
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            LabeledTextField(
                modifier = Modifier.weight(1f),
                label = "Postal Code",
                placeholder = "e.g. 110001",
                value = postal,
                onValueChange = {
                    postal = it
                    onAddressChange(ui.address?.copy(postalCode = postal)
                        ?: CheckoutAddress(postalCode = postal))
                }
            )

            LabeledTextField(
                modifier = Modifier.weight(1f),
                label = "Country",
                value = country,
                onValueChange = {
                    country = it
                    onAddressChange(ui.address?.copy(country = country)
                        ?: CheckoutAddress(country = country))
                }
            )
        }

        Spacer(Modifier.height(14.dp))
    }
}

@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    value: String,
    onValueChange: (String) -> Unit
) {
    val blue = Color(0xFF003466)

    Column(modifier) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = blue,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = blue,
                cursorColor = blue
            )
        )
    }
}