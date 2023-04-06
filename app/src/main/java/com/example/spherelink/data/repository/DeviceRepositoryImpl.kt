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

    override fun getDevicesAsList(): List<DeviceEntity>
    {
        return deviceDao.getDevicesAsList()
    }

    override suspend fun getDistance(address: String): Int {
        return deviceDao.getDistance(address)
    }

    override suspend fun getBatteryLevel(address: String): Int {
        return deviceDao.getBatteryLevel(address)
    }

    override suspend fun updateRssi(address: String, rssi: Int) {
        deviceDao.updateRssi(address, rssi)
    }

    override suspend fun updateDistance(address: String, distance: Int) {
        deviceDao.updateDistance(address, distance)
    }

    override suspend fun updateIsConnected(address: String, isConnected: Boolean) {
        deviceDao.updateIsConnected(address, isConnected)
    }

    override suspend fun updateBatteryLevel(address: String, batteryLevel: Int) {
        deviceDao.updateBatteryLevel(address, batteryLevel)
    }
}