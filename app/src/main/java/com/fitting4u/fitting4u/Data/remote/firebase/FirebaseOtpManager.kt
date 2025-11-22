package com.fitting4u.fitting4u.Data.remote.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseOtpManager @Inject constructor(
    private val auth: FirebaseAuth
) {

    // Send verification SMS; returns verificationId or throws
    suspend fun sendOtp(activity: Activity, phone: String): String = suspendCancellableCoroutine { cont ->
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                // Auto-retrieval; not returning verificationId here. Let caller handle.
                Log.d("FirebaseOtpManager", "onVerificationCompleted")
            }


            override fun onVerificationFailed(e: FirebaseException) {
                if (!cont.isCompleted) cont.resumeWithException(e)
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                if (!cont.isCompleted) cont.resume(verificationId)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        cont.invokeOnCancellation {
            // nothing
        }
    }

    // Verify OTP using verificationId + code -> returns Firebase credential (or throw)
    suspend fun verifyOtp(verificationId: String, code: String) = suspendCancellableCoroutine<com.google.firebase.auth.AuthResult> { cont ->
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    if (!cont.isCompleted) cont.resume(result)
                }
                .addOnFailureListener { e ->
                    if (!cont.isCompleted) cont.resumeWithException(e)
                }
        } catch (e: Exception) {
            cont.resumeWithException(e)
        }
    }
}