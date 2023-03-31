package com.example.spherelink.data.repository

import com.example.spherelink.data.entities.DeviceEntity
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun insertDevice(device: DeviceEntity): Long
    suspend fun deleteDevice(device: DeviceEntity)
    suspend fun getDeviceByAddress(address: String): DeviceEntity
    fun getAllDevices(): Flow<List<DeviceEntity>>
}