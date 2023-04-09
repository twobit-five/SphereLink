package com.example.spherelink.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spherelink.data.dao.DeviceDao
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.RssiValue

@Database(entities = [DeviceEntity::class, RssiValue::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
}
