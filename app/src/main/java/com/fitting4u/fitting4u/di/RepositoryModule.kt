package com.fitting4u.fitting4u.di

import com.fitting4u.fitting4u.Data.local.room.Cart.CartDao
import com.fitting4u.fitting4u.Data.local.room.Config.ConfigDao
import com.fitting4u.fitting4u.Data.local.room.Fabric.Home.FabricHomeDao
import com.fitting4u.fitting4u.Data.remote.api.CartApi
import com.fitting4u.fitting4u.Data.remote.api.ConfigApi
import com.fitting4u.fitting4u.Data.remote.api.FabricApi
import com.fitting4u.fitting4u.Data.remote.firebase.FirebaseConfigSource
import com.fitting4u.fitting4u.Data.repository.CartRepositoryImpl
import com.fitting4u.fitting4u.Data.repository.ConfigRepositoryImpl
import com.fitting4u.fitting4u.Data.repository.FabricRepositoryImpl
import com.fitting4u.fitting4u.data.remote.firebase.FirebaseFabricHomeSource
import com.fitting4u.fitting4u.domain.repository.CartRepository
import com.fitting4u.fitting4u.domain.repository.ConfigRepository
import com.fitting4u.fitting4u.domain.repository.FabricRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideConfigRepository(
        api: ConfigApi,
        firebase: FirebaseConfigSource,
        dao: ConfigDao,
        gson: Gson
    ): ConfigRepository {
        return ConfigRepositoryImpl(
            api = api,
            firebase = firebase,
            dao = dao,
            gson = gson
        )
    }

    @Provides
    @Singleton
    fun provideFabricRepository(
        api: FabricApi,
        firebase: FirebaseFabricHomeSource,
        dao: FabricHomeDao,
        gson: Gson
    ): FabricRepository {
        return FabricRepositoryImpl(
            api = api,
            firebase = firebase,
            dao = dao,
            gson = gson
        )
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        dao: CartDao,
        api: CartApi
    ): CartRepository {
        return CartRepositoryImpl(dao, api = api)
    }
}