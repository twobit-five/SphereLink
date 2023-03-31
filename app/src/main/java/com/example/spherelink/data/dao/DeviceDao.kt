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
}
