package com.fitting4u.fitting4u.Data.remote.firebase

import android.util.Log
import com.fitting4u.fitting4u.Data.remote.dto.Config.ConfigDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseConfigSource(
    private val firestore: FirebaseFirestore
) {

    suspend fun getConfig(): ConfigDto =
        suspendCancellableCoroutine { cont ->


            firestore.collection("fitting4u_config")
                .document("main")
                .get()
                .addOnSuccessListener { snapshot ->


                    val config = snapshot.toObject(ConfigDto::class.java)
                    if (config == null) {
                        cont.resumeWithException(Exception("Firebase parsing error"))
                    } else {
                        cont.resume(config)
                    }
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }
}