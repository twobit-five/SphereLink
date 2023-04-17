package com.example.spherelink.data.repository

import com.example.spherelink.data.dao.DeviceDao
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.RssiValue
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.flow.Flow

class DeviceRepositoryImpl (
    private val dao: DeviceDao
): DeviceRepository {

    override suspend fun insertDevice(device: DeviceEntity): Long {
        return dao.insertDevice(device)
    }

    override suspend fun deleteDevice(device: DeviceEntity) {
        dao.deleteDevice(device)
    }

    override suspend fun getDeviceByAddress(address: String): DeviceEntity {
        return dao.getDeviceByAddress(address)
    }

    override fun getAllDevices(): Flow<List<DeviceEntity>> {
        return dao.getAllDevices()
    }

    override fun getDevicesAsList(): List<DeviceEntity>
    {
        return dao.getDevicesAsList()
    }

    override suspend fun getDistance(address: String): Int {
        return dao.getDistance(address)
    }

    override suspend fun getBatteryLevel(address: String): Int {
        return dao.getBatteryLevel(address)
    }

    override suspend fun updateDeviceName(address: String, deviceName: String) {
        dao.updateDeviceName(address, deviceName)
    }

    override suspend fun updateRssi(address: String, rssi: Int) {
        dao.updateRssi(address, rssi)
    }

    override suspend fun updateDistance(address: String, distance: Int) {
        dao.updateDistance(address, distance)
    }

    override suspend fun updateIsConnected(address: String, isConnected: Boolean) {
        dao.updateIsConnected(address, isConnected)
    }

    override suspend fun updateBatteryLevel(address: String, batteryLevel: Int) {
        dao.updateBatteryLevel(address, batteryLevel)
    }


    override suspend fun insertRssiValueWithLimit(rssiValue: RssiValue, limit: Int) {
        val rssiValues = dao.getDeviceHistoryList(rssiValue.deviceAddress)
        if (rssiValues.size >= limit) {
            val toDelete = rssiValues.size - limit + 1
            for (i in 0 until toDelete) {
                dao.deleteRssiValue(rssiValues[i].id)
            }
        }
        dao.insertRssiValue(rssiValue)
    }

    override fun getDeviceHistory(deviceAddress: String): Flow<List<RssiValue>> {
        return dao.getDeviceHistory(deviceAddress)
    }
}