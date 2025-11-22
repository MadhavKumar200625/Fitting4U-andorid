package com.fitting4u.fitting4u.di

import com.fitting4u.fitting4u.Data.remote.firebase.FirebaseConfigSource
import com.fitting4u.fitting4u.data.remote.firebase.FirebaseFabricHomeSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFabricHomeSource(
        firestore: FirebaseFirestore
    ): FirebaseFabricHomeSource {
        return FirebaseFabricHomeSource(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseConfigSource(
        firestore: FirebaseFirestore
    ): FirebaseConfigSource {
        return FirebaseConfigSource(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()
}