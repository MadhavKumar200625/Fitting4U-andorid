package com.fitting4u.fitting4u.di

import android.content.Context
import androidx.room.Room
import com.fitting4u.fitting4u.Data.local.room.AppDatabase
import com.fitting4u.fitting4u.Data.local.room.Cart.CartDao

import com.fitting4u.fitting4u.Data.local.room.Config.ConfigDao
import com.fitting4u.fitting4u.Data.local.room.Fabric.Home.FabricHomeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "fitting4u.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideConfigDao(db: AppDatabase): ConfigDao = db.configDao

    @Provides
    @Singleton
    fun provideFabricHomeDao(db: AppDatabase): FabricHomeDao = db.fabricHomeDao

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao
}