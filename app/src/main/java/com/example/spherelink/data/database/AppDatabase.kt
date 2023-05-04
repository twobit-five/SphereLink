package com.example.spherelink.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spherelink.data.dao.DeviceDao
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.DeviceRssiStats
import com.example.spherelink.data.entities.DeviceRssiHistory

@Database(entities = [DeviceEntity::class, DeviceRssiHistory::class, DeviceRssiStats::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
}
