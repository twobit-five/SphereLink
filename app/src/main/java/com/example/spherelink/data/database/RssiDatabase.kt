package com.example.spherelink.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spherelink.data.dao.DeviceDao
import com.example.spherelink.data.entities.DeviceEntity

@Database(entities = [DeviceEntity::class], version = 1, exportSchema = false)
abstract class RssiDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
}
