package com.example.spherelink.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rssi_stats")
data class DeviceRssiStats(
    @PrimaryKey val deviceAddress: String,
    val avgRssi: Int,
    val stdDev: Double,
    val variance: Double,
    val median: Int,
    val range: Int,
    val min: Int,
    val max: Int,
    val mode: Int,
    val count: Int
)