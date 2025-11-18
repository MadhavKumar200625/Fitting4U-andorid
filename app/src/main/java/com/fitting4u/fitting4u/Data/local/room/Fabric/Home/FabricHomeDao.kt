package com.fitting4u.fitting4u.Data.local.room.Fabric.Home

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FabricHomeDao {

    @Query("SELECT * FROM fabric_home WHERE id = 1 LIMIT 1")
    suspend fun getFabricHome(): FabricHomeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFabricHome(data: FabricHomeEntity)
}