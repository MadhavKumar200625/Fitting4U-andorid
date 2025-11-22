package com.fitting4u.fitting4u.presentation.common.sheet

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.fitting4u.fitting4u.presentation.common.UserViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * LoginBottomSheet (fixed):
 * - register BroadcastReceiver immediately when starting SMS-user-consent
 * - keep localSending flag so UI disables buttons while waiting
 * - reliable focus + keyboard for hidden input field (OTP typing)
 */

@Composable
fun LoginBottomSheet(
    vm: UserViewModel,
    onSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current

    var otp by remember { mutableStateOf("") }
    val activity = context as? Activity

    // local flag to mark that we've initiated a send (so UI disables instantly)
    var localSending by remember { mutableStateOf(false) }

    // effective loading used for button enabling
    val effectiveLoading = remember(ui.isLoading, localSending) { ui.isLoading || localSending }

    // ---------------- CONSENT LAUNCHER ----------------
    val consentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        try {
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val message = result.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                if (!message.isNullOrEmpty()) {
                    val code = extractOtpFromMessage(message)
                    if (code != null) {
                        otp = code
                        // call your ViewModel verify (keeps logic unchanged)
                        vm.verifyOtpAndLogin(code)
                    }
                }
            } else {
                Log.d("LoginBottomSheet", "Consent cancelled or no data")
            }
        } catch (e: Exception) {
            Log.e("LoginBottomSheet", "Consent result error", e)
        }
    }

    // ---------------- SMS BROADCAST RECEIVER ----------------
    val smsReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                try {
                    if (intent == null) return
                    if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                        val extras = intent.extras
                        val consentIntent = extras?.get(SmsRetriever.EXTRA_CONSENT_INTENT) as? Intent
                        if (consentIntent != null && activity != null) {
                            consentLauncher.launch(consentIntent)
                        } else {
                            Log.w("LoginBottomSheet", "No consent intent or activity null")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginBottomSheet", "SMS receiver error", e)
                }
            }
        }
    }

    // register/unregister receiver in a lifecycle-safe way (we register immediately when starting consent)
    var receiverRegistered by remember { mutableStateOf(false) }
    DisposableEffect(receiverRegistered) {
        onDispose {
            if (receiverRegistered) {
                try {
                    context.unregisterReceiver(smsReceiver)
                } catch (_: Exception) {}
                receiverRegistered = false
            }
        }
    }

    // ---------------- helper functions ----------------
    fun startSmsConsentAndRegisterReceiver() {
        try {
            // register receiver immediately (so we don't miss the SMS)
            if (!receiverRegistered) {
                val filter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
                try {
                    ContextCompat.registerReceiver(
                        context,
                        smsReceiver,
                        filter,
                        ContextCompat.RECEIVER_NOT_EXPORTED
                    )
                    receiverRegistered = true
                } catch (e: Exception) {
                    Log.e("LoginBottomSheet", "registerReceiver failed", e)
                }
            }

            val client = SmsRetriever.getClient(context)
            client.startSmsUserConsent(null) // listen to next incoming SMS from any sender
            Log.d("LoginBottomSheet", "Sms User Consent started")
        } catch (e: Exception) {
            Log.e("LoginBottomSheet", "startSmsUserConsent failed", e)
        }
    }

    fun sendOTP(isResend: Boolean = false) {
        val act = context as? Activity ?: return vm.sendOtp(null)
        val raw = ui.phone.ifBlank { return vm.sendOtp(null) }

        // set local sending so UI disables instantly while we wait for firebase callback
        localSending = true

        val phone = if (raw.startsWith("+")) raw else "+91$raw"
        val auth = FirebaseAuth.getInstance()

        // start consent and register receiver immediately
        startSmsConsentAndRegisterReceiver()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // no change to your logic
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // revert local sending; ViewModel will update error too
                localSending = false
                vm.sendOtp(null)
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                // firebase callback will update ViewModel -> ui.isOtpSent = true
                // we keep localSending true for a short while; UI will react to vm.ui changes below
                if (isResend) vm.sendOtpAgain(id) else vm.sendOtp(id)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(act)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // clear localSending when VM indicates work finished or error arrived
    LaunchedEffect(ui.isOtpSent, ui.error, ui.isLoading) {
        if (ui.isOtpSent || ui.error != null || ui.isLoading) {
            // let VM control final loading, but clear local flag if VM took over
            localSending = localSending && (!ui.isOtpSent && ui.error == null && !ui.isLoading)
        } else {
            localSending = false
        }
    }

    // ---------------- PURE BOTTOM-SHEET UI WRAPPER ----------------
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.78f)
                .imePadding()              // 👈 FIX: allows keyboard to open
                .navigationBarsPadding()   // 👈 Fixes Android 13+ behavior
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(22.dp)
            ) {

                // Close icon — onDismiss handled by caller (AppNavigation hides sheet)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = {
                        // ensure we stop local sending and tell caller to dismiss
                        localSending = false
                        onDismiss()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Close Login", tint = Color.DarkGray)
                    }
                }

                Text(
                    "Let’s get you started 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF003466)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    "Enter your mobile number to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(26.dp))

                if (!ui.isOtpSent) {

                    PhoneInputStage(
                        phone = ui.phone,
                        isLoading = effectiveLoading,
                        error = ui.error,
                        onPhoneChange = vm::onPhoneChanged,
                        onContinue = { sendOTP(false) }
                    )

                } else {

                    OtpInputStage(
                        otp = otp,
                        onOtpChange = { otp = it },
                        isLoading = effectiveLoading,
                        error = ui.error,
                        onSubmit = { vm.verifyOtpAndLogin(it) },
                        onResend = { sendOTP(true) }
                    )
                }

                if (ui.isSuccess) {
                    LaunchedEffect(Unit) {
                        // clean up and notify success
                        localSending = false
                        onSuccess()
                    }
                }
            }
        }
    }
}

/**
 * Extracts first 6-digit sequence from the incoming SMS text.
 */
private fun extractOtpFromMessage(message: String): String? {
    val p = Pattern.compile("\\b(\\d{6})\\b")
    val m = p.matcher(message)
    return if (m.find()) m.group(1) else null
}