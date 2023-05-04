package com.example.spherelink.domain.repo

import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.DeviceRssiStats
import com.example.spherelink.data.entities.DeviceRssiHistory
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun insertDeviceEntity(device: DeviceEntity): Long

    suspend fun deleteDeviceEntity(device: DeviceEntity)

    fun getDeviceEntityByAddress(address: String): Flow<DeviceEntity>

    fun getAllDeviceEntities(): Flow<List<DeviceEntity>>

    fun getDeviceEntitiesAsList(): List<DeviceEntity>

    suspend fun getBatteryLevel(address: String): Int
    suspend fun updateDeviceEntityDeviceName(address: String, deviceName: String)
    suspend fun updateDeviceEntityRssi(address: String, rssi: Int)

    suspend fun updateDeviceEntityDistance(address: String, distance: Int)

    suspend fun updateDeviceEntitiyIsConnected(address: String, isConnected: Boolean)

    suspend fun getDistance(address: String): Int

    suspend fun updateDeviceEntityBatteryLevel(address: String, batteryLevel: Int)

    suspend fun insertRssiValueWithLimit(deviceRssiHistory: DeviceRssiHistory, limit: Int)

    fun getDeviceHistory(deviceAddress: String): Flow<List<DeviceRssiHistory>>

    suspend fun getDeviceHistoryList(deviceAddress: String): List<DeviceRssiHistory>

    suspend fun deleteRssiValues(rssivalues: Int)

    suspend fun deleteOldRssiValues(deviceAddress: String, limit: Long)

    //suspend fun deleteAllRssiValues()

    suspend fun insertRssiStat(deviceRssiStats: DeviceRssiStats)

    fun getRssiStats(deviceAddress: String): Flow<DeviceRssiStats>
}