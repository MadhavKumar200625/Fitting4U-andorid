package com.fitting4u.fitting4u.presentation.common.sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun OtpInputStage(
    otp: String,
    onOtpChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    onSubmit: (String) -> Unit,
    onResend: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {

        OtpTextField(
            otp = otp,
            onOtpChange = onOtpChange,
            isEnabled = !isLoading
        )

        if (error != null) {
            Spacer(Modifier.height(6.dp))
            Text(error, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { onSubmit(otp) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = otp.length == 6 && !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003466))
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            else Text("Verify OTP", color = Color.White)
        }

        Spacer(Modifier.height(14.dp))

        TextButton(onClick = onResend, enabled = !isLoading) {
            Text("Resend OTP", color = Color(0xFF003466))
        }
    }
}

/**
 * SIMPLE + PERFECT:
 * - Real focusable hidden field
 * - Shown keyboard automatically
 * - Tapping box = focuses field
 */
@Composable
fun OtpTextField(
    otp: String,
    onOtpChange: (String) -> Unit,
    isEnabled: Boolean
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    // 🔥 Show keyboard automatically when first loaded
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboard?.show()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {

        // Hidden actual input (REAL FOCUSABLE FIELD)
        BasicTextField(
            value = otp,
            onValueChange = {
                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                    onOtpChange(it)
                }
            },
            textStyle = TextStyle(color = Color.Transparent),
            modifier = Modifier
                .matchParentSize()
                .focusRequester(focusRequester)     // ⭐ REAL FOCUS
                .clickable {
                    focusRequester.requestFocus()
                    keyboard?.show()
                },
            enabled = isEnabled,
            cursorBrush = androidx.compose.ui.graphics.Brush.verticalGradient(
                listOf(Color.Transparent, Color.Transparent)
            )
        )

        // Visible boxes
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(6) { index ->
                val char = otp.getOrNull(index)?.toString() ?: ""

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .border(
                            width = 2.dp,
                            color = if (char.isNotEmpty()) Color(0xFF003466)
                            else Color.Gray.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            focusRequester.requestFocus()
                            keyboard?.show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        char,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }
            }
        }
    }
}