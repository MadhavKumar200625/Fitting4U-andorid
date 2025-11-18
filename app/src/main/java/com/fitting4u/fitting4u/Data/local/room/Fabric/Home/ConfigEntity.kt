package com.fitting4u.fitting4u.Data.local.room.Fabric.Home

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fabric_home")
data class FabricHomeEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
    val updatedAt: String
)