package com.example.spherelink.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO add distance and timestamp fields.
@Entity(tableName = "rssi_table")
data class DeviceEntity(
    @PrimaryKey val address: String,
    val rssi: Int,
    val distance: Int,
    val timestamp: Long
)