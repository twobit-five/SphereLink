package com.example.spherelink.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spherelink.data.entities.DeviceEntity

@Dao
interface DeviceDao {

    @Query("SELECT * FROM rssi_table")
    fun getAllDevices(): LiveData<List<DeviceEntity>>

    //TODO I think there is an annotation @Upsert that can be used here instead of @Insert.
    // That would insert if doesnt exist and update if it does.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(deviceEntity: DeviceEntity): Long

    @Query("DELETE FROM rssi_table WHERE address = :address")
    fun deleteDeviceByAddress(address: String)

    //TODO Update class after distance and timestamp fields are added to the DeviceEntity class.
}
