package com.example.spherelink.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_table")
data class DeviceEntity(
    @PrimaryKey val address: String,
    val name: String,
    val rssi: Int,
    val distance: Int,
    val isDone: Boolean,
    val isConnected: Boolean,
    val timestamp: Long,
    val batteryLevel: Int,
    val avgRSSI: Int
)