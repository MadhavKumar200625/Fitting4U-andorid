package com.fitting4u.fitting4u.Data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fitting4u.fitting4u.Data.local.room.Cart.CartDao
import com.fitting4u.fitting4u.Data.local.room.Cart.CartItem
import com.fitting4u.fitting4u.Data.local.room.Config.ConfigDao
import com.fitting4u.fitting4u.Data.local.room.Config.ConfigEntity
import com.fitting4u.fitting4u.Data.local.room.Fabric.Home.FabricHomeDao
import com.fitting4u.fitting4u.Data.local.room.Fabric.Home.FabricHomeEntity

@Database(
    entities = [
        ConfigEntity::class,
        FabricHomeEntity::class,
        CartItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val configDao: ConfigDao
    abstract val fabricHomeDao: FabricHomeDao
    abstract val cartDao : CartDao

}