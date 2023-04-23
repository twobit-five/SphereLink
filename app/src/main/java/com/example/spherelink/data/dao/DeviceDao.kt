package com.example.spherelink.data.dao

import androidx.room.*
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.RssiValue
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {

    //TODO need to catch failure and throw a toast when device exists.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDevice(deviceEntity: DeviceEntity): Long

    @Delete
    suspend fun deleteDevice(deviceEntity: DeviceEntity)

    @Query("SELECT * FROM device_table WHERE address = :address")
    fun getDeviceByAddress(address: String): DeviceEntity

    @Query("SELECT * FROM device_table")
    fun getAllDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM device_table")
    fun getDevicesAsList(): List<DeviceEntity>

    @Query("SELECT distance FROM device_table WHERE address = :address")
    suspend fun getDistance(address: String): Int

    @Query("SELECT batteryLevel FROM device_table WHERE address = :address")
    suspend fun getBatteryLevel(address: String): Int

    @Query("Update device_table SET device_name = :deviceName WHERE address = :address")
    suspend fun updateDeviceName(address: String, deviceName: String)

    @Query("UPDATE device_table SET rssi = :rssi, timestamp = :timestamp WHERE address = :address")
    suspend fun updateRssi(address: String, rssi: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE device_table SET distance = :distance WHERE address = :address")
    suspend fun updateDistance(
        address: String,
        distance: Int,
    )

    @Query("UPDATE device_table SET isConnected = :isConnected WHERE address = :address")
    suspend fun updateIsConnected(address: String, isConnected: Boolean)

    @Query("UPDATE device_table SET batteryLevel = :batteryLevel, timestamp = :timestamp WHERE address = :address")
    suspend fun updateBatteryLevel(
        address: String,
        batteryLevel: Int,
        timestamp: Long = System.currentTimeMillis()
    )


    // Device History Table

    @Query("SELECT * FROM device_history WHERE deviceAddress = :deviceAddress ORDER BY timestamp DESC")
    fun getDeviceHistory(deviceAddress: String): Flow<List<RssiValue>>

    @Query("SELECT * FROM device_history WHERE deviceAddress = :deviceAddress ORDER BY timestamp DESC")
    fun getDeviceHistoryList(deviceAddress: String): List<RssiValue>

    @Insert
    fun insertRssiValue(rssiValue: RssiValue)

    @Query("DELETE FROM device_history WHERE id = :id")
    fun deleteRssiValue(id: Int)

    @Query("DELETE FROM device_history WHERE rssi =:rssiValue")
    fun deleteRssiValues(rssiValue: Int)

    @Query("DELETE FROM device_history WHERE deviceAddress = :deviceAddress AND timestamp < :threshold")
    suspend fun deleteOldRssiValues(deviceAddress: String, threshold: Long)
}