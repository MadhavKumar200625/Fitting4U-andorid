package com.fitting4u.fitting4u.di


import com.fitting4u.fitting4u.Data.remote.api.ConfigApi
import com.fitting4u.fitting4u.Data.remote.api.FabricApi
import retrofit2.Retrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideConfigApi(retrofit: Retrofit): ConfigApi {
        return retrofit.create(ConfigApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFabricApi(retrofit: Retrofit): FabricApi {
        return retrofit.create(FabricApi::class.java)
    }
}