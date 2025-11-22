package com.fitting4u.fitting4u.di


import com.fitting4u.fitting4u.Data.remote.api.CartApi
import com.fitting4u.fitting4u.Data.remote.api.ConfigApi
import com.fitting4u.fitting4u.Data.remote.api.FabricApi
import com.fitting4u.fitting4u.Data.remote.api.UserApi
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

    @Provides
    @Singleton
    fun provideCartApi(retrofit: Retrofit): CartApi = retrofit.create(CartApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
}