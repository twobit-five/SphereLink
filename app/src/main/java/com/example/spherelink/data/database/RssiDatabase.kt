package com.example.spherelink.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spherelink.data.dao.DeviceDao
import com.example.spherelink.data.entities.DeviceEntity

@Database(entities = [DeviceEntity::class], version = 1, exportSchema = false)
abstract class RssiDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    companion object {

        @Volatile
        private var INSTANCE: RssiDatabase? = null

        fun getInstance(context: Context): RssiDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RssiDatabase::class.java,
                    "rssi_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
