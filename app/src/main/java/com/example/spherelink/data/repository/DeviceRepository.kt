package com.example.spherelink.data.repository

import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.RssiValue
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun insertDevice(device: DeviceEntity): Long

    suspend fun deleteDevice(device: DeviceEntity)

    suspend fun getDeviceByAddress(address: String): DeviceEntity

    fun getAllDevices(): Flow<List<DeviceEntity>>

    fun getDevicesAsList(): List<DeviceEntity>

    suspend fun getBatteryLevel(address: String): Int

    suspend fun updateRssi(address: String, rssi: Int)

    suspend fun updateDistance(address: String, distance: Int)

    suspend fun updateIsConnected(address: String, isConnected: Boolean)

    suspend fun getDistance(address: String): Int

    suspend fun updateBatteryLevel(address: String, batteryLevel: Int)

    suspend fun insertRssiValueWithLimit(rssiValue: RssiValue, limit: Int)

    suspend fun getRssiValuesForDevice(deviceAddress: String): List<RssiValue>
}