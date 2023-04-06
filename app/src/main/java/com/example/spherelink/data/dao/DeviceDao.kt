package com.example.spherelink.data.dao

import androidx.room.*
import com.example.spherelink.data.entities.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(deviceEntity: DeviceEntity): Long

    @Delete
    suspend fun deleteDevice(deviceEntity: DeviceEntity)

    @Query("SELECT * FROM rssi_table WHERE address = :address")
    fun getDeviceByAddress(address: String): DeviceEntity

    @Query("SELECT * FROM rssi_table")
    fun getAllDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM rssi_table")
    fun getDevicesAsList(): List<DeviceEntity>

    @Query("SELECT distance FROM rssi_table WHERE address = :address")
    suspend fun getDistance(address: String): Int

    @Query("SELECT batteryLevel FROM rssi_table WHERE address = :address")
    suspend fun getBatteryLevel(address: String): Int

    @Query("UPDATE rssi_table SET rssi = :rssi, timestamp = :timestamp WHERE address = :address")
    suspend fun updateRssi(address: String, rssi: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE rssi_table SET distance = :distance, timestamp = :timestamp WHERE address = :address")
    suspend fun updateDistance(address: String, distance: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE rssi_table SET isConnected = :isConnected, timestamp = :timestamp WHERE address = :address")
    suspend fun updateIsConnected(address: String, isConnected: Boolean, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE rssi_table SET batteryLevel = :batteryLevel, timestamp = :timestamp WHERE address = :address")
    suspend fun updateBatteryLevel(address: String, batteryLevel: Int, timestamp: Long = System.currentTimeMillis())
}
