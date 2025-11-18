package com.fitting4u.fitting4u.presentation


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun LoginPopup(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isOtpSent by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier.width(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    if (!isOtpSent) "Login with Phone" else "Enter OTP",
                    color = Color(0xFF003466),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(16.dp))

                if (!isOtpSent) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        placeholder = { Text("+91 9876543210") }
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (phone.isNotBlank()) {
                                sendOtp(
                                    auth = auth,
                                    phone = phone,
                                    context = context,
                                    onSent = { id ->
                                        verificationId = id
                                        isOtpSent = true
                                        loading = false
                                    },
                                    onFailed = {
                                        loading = false
                                        Log.e("AUTH", "OTP failed: ${it.message}")
                                    }
                                )
                            }
                        },
                        enabled = !loading
                    ) {
                        Text(if (!loading) "Send OTP" else "Sending...")
                    }
                } else {
                    OutlinedTextField(
                        value = otp,
                        onValueChange = { otp = it },
                        label = { Text("OTP") }
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (verificationId != null && otp.isNotBlank()) {
                                verifyOtp(
                                    auth = auth,
                                    verificationId = verificationId!!,
                                    otp = otp,
                                    context = context,
                                    onSuccess = {
                                        saveUser(context, phone)
                                        onSuccess()
                                    },
                                    onFailed = {
                                        Log.e("AUTH", "Verify failed: ${it.message}")
                                    }
                                )
                            }
                        }
                    ) {
                        Text("Verify OTP")
                    }
                }

                Spacer(Modifier.height(12.dp))

                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    }
}

fun sendOtp(
    auth: FirebaseAuth,
    phone: String,
    context: Context,
    onSent: (String) -> Unit,
    onFailed: (Exception) -> Unit
) {
    val activity = context as Activity

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Instant verification or auto-retrieval
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(context, e.message ?: "Unknown error", Toast.LENGTH_LONG).show()
            onFailed(e)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            onSent(verificationId)
        }
    }

    PhoneAuthProvider.getInstance().verifyPhoneNumber(
        phone,
        60,
        TimeUnit.SECONDS,
        activity,
        callbacks
    )
}

fun verifyOtp(
    auth: FirebaseAuth,
    verificationId: String,
    otp: String,
    context: Context,
    onSuccess: () -> Unit,
    onFailed: (Exception) -> Unit
) {
    val credential = PhoneAuthProvider.getCredential(verificationId, otp)

    auth.signInWithCredential(credential)
        .addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailed(task.exception ?: Exception("Unknown error"))
            }
        }
}

fun saveUser(context: Context, phone: String) {
    val shared: SharedPreferences = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
    val editor = shared.edit()
    editor.putString("phone", phone)
    editor.putBoolean("loggedIn", true)
    editor.apply()
}

fun isUserLoggedIn(context: Context): Boolean {
    val shared: SharedPreferences = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
    return shared.getBoolean("loggedIn", false)
}