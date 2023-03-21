package com.example.spherelink.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.spherelink.data.database.RssiDatabase
import com.example.spherelink.data.entities.DeviceEntity

class DeviceRepository (context: Context) {
    private val database = RssiDatabase.getInstance(context)
    private val deviceDao = database.deviceDao()

    val allDevices: LiveData<List<DeviceEntity>> = deviceDao.getAllDevices()

    suspend fun insertDevice(deviceEntity: DeviceEntity): Long {
        return deviceDao.insertDevice(deviceEntity)
    }

    suspend fun deleteDevice(address: String) {
        deviceDao.deleteDeviceByAddress(address)
    }


    //TODO update after adding new fields to DeviceEntity
}