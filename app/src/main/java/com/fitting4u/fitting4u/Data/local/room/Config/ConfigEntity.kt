package com.fitting4u.fitting4u.Data.local.room.Config

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class ConfigEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
    val updatedAt: String
)