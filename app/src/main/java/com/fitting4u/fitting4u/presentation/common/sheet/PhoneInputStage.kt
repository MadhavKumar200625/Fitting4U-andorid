package com.fitting4u.fitting4u.presentation.common.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PhoneInputStage(
    phone: String,
    isLoading: Boolean,
    error: String?,
    onPhoneChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = phone,
            onValueChange = {
                // only digits and up to 10 chars (UI-level validation). Keep as before.
                val onlyDigits = it.filter { ch -> ch.isDigit() }
                if (onlyDigits.length <= 10) onPhoneChange(onlyDigits)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            leadingIcon = {
                androidx.compose.material3.Text("+91", color = Color.Black)
            },
            shape = RoundedCornerShape(14.dp),
            textStyle = MaterialTheme.typography.titleMedium,
            label = { Text("Mobile Number") }
        )

        if (error != null) {
            Spacer(Modifier.height(6.dp))
            Text(error, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { if (!isLoading) onContinue() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003466))
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Continue")
            }
        }
    }
}