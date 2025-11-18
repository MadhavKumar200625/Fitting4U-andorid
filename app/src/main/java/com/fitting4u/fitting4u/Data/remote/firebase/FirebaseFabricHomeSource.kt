package com.fitting4u.fitting4u.data.remote.firebase

import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.FabricHomeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseFabricHomeSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getFabricHome(): FabricHomeDto =
        suspendCancellableCoroutine { continuation ->

            firestore
                .collection("fabric_home")
                .document("main")
                .get()
                .addOnSuccessListener { doc ->

                    if (!doc.exists()) {
                        continuation.resumeWithException(
                            Exception("Fabric Home document does not exist")
                        )
                        return@addOnSuccessListener
                    }

                    val dto = doc.toObject(FabricHomeDto::class.java)
                    if (dto != null) {
                        continuation.resume(dto)
                    } else {
                        continuation.resumeWithException(
                            Exception("Firebase parsing error")
                        )
                    }
                }
                .addOnFailureListener { error ->
                    continuation.resumeWithException(error)
                }
        }
}