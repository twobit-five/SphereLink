package com.example.spherelink.data.repository

import com.example.spherelink.data.dao.DeviceDao
import com.example.spherelink.data.entities.DeviceEntity
import kotlinx.coroutines.flow.Flow

class DeviceRepositoryImpl (
    private val deviceDao: DeviceDao
): DeviceRepository {

    override suspend fun insertDevice(device: DeviceEntity): Long {
        return deviceDao.insertDevice(device)
    }

    override suspend fun deleteDevice(device: DeviceEntity) {
        deviceDao.deleteDevice(device)
    }

    override suspend fun getDeviceByAddress(address: String): DeviceEntity {
        return deviceDao.getDeviceByAddress(address)
    }

    override fun getAllDevices(): Flow<List<DeviceEntity>> {
        return deviceDao.getAllDevices()
    }
}