package com.example.spherelink.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_history")
data class RssiValue (
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val timestamp: Long,
    val deviceAddress: String,
    val rssi: Int
)