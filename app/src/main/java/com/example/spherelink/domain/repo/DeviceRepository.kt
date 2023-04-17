package com.example.spherelink.domain.repo

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
    suspend fun updateDeviceName(address: String, deviceName: String)
    suspend fun updateRssi(address: String, rssi: Int)

    suspend fun updateDistance(address: String, distance: Int)

    suspend fun updateIsConnected(address: String, isConnected: Boolean)

    suspend fun getDistance(address: String): Int

    suspend fun updateBatteryLevel(address: String, batteryLevel: Int)

    suspend fun insertRssiValueWithLimit(rssiValue: RssiValue, limit: Int)

    fun getDeviceHistory(deviceAddress: String): Flow<List<RssiValue>>
}