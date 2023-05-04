package com.example.spherelink.data.repository

import com.example.spherelink.data.dao.DeviceDao
import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.entities.DeviceRssiStats
import com.example.spherelink.data.entities.DeviceRssiHistory
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.flow.Flow

//TODO this class needs cleaned up


class DeviceRepository (
    private val dao: DeviceDao
): DeviceRepository {

    // Device Entity Table Queries


    override suspend fun insertDeviceEntity(device: DeviceEntity): Long {
        return dao.insertDevice(device)
    }

    override suspend fun deleteDeviceEntity(device: DeviceEntity) {
        dao.deleteDevice(device)
    }

    //TODO needs to suspend but view model needs corrosponding change
    override fun getDeviceEntityByAddress(address: String): Flow<DeviceEntity> {
        return dao.getDeviceByAddress(address)
    }

    override fun getAllDeviceEntities(): Flow<List<DeviceEntity>> {
        return dao.getAllDevices()
    }

    override fun getDeviceEntitiesAsList(): List<DeviceEntity> {
        return dao.getDevicesAsList()
    }

    override suspend fun getDistance(address: String): Int {
        return dao.getDistance(address)
    }

    override suspend fun getBatteryLevel(address: String): Int {
        return dao.getBatteryLevel(address)
    }

    override suspend fun updateDeviceEntityDeviceName(address: String, deviceName: String) {
        dao.updateDeviceName(address, deviceName)
    }

    override suspend fun updateDeviceEntityRssi(address: String, rssi: Int) {
        dao.updateRssi(address, rssi)
    }


    override suspend fun updateDeviceEntityDistance(address: String, distance: Int) {
        dao.updateDistance(address, distance)
    }

    override suspend fun updateDeviceEntitiyIsConnected(address: String, isConnected: Boolean) {
        dao.updateIsConnected(address, isConnected)
    }

    override suspend fun updateDeviceEntityBatteryLevel(address: String, batteryLevel: Int) {
        dao.updateBatteryLevel(address, batteryLevel)
    }


    // DeviceRssiHistory Table Queries

    override suspend fun insertRssiValueWithLimit(deviceRssiHistory: DeviceRssiHistory, limit: Int) {
        val rssiValues = dao.getDeviceHistoryList(deviceRssiHistory.deviceAddress!!)
        if (rssiValues.size >= limit) {
            val toDelete = rssiValues.size - limit + 1
            for (i in 0 until toDelete) {
                dao.deleteRssiValue(rssiValues[i].id)
            }
        }
        dao.insertRssiValue(deviceRssiHistory)
    }

    //TODO this needs to suspend. but the view model need to change which calls it.
    override fun getDeviceHistory(deviceAddress: String): Flow<List<DeviceRssiHistory>> {
        return dao.getDeviceHistory(deviceAddress)
    }

    override suspend fun getDeviceHistoryList(deviceAddress: String): List<DeviceRssiHistory> {
        return dao.getDeviceHistoryList(deviceAddress)
    }

    override suspend fun deleteRssiValues(rssi: Int) {
        dao.deleteRssiValues(rssi)
    }

    override suspend fun deleteOldRssiValues(deviceAddress: String, limit: Long) {
        dao.deleteOldRssiValues(deviceAddress, limit)
    }

    // DeviceRssiStats Table Queries

    override suspend fun insertRssiStat(deviceRssiStats: DeviceRssiStats) {
        dao.insertRssiStats(deviceRssiStats)

        //update avg rssi value for device entiy
        dao.updateAvgRssi(deviceRssiStats.deviceAddress!!, deviceRssiStats.avgRssi)
    }

    //TODO this needs to suspend. but the view model need to change which calls it.
    override fun getRssiStats(deviceAddress: String): Flow<DeviceRssiStats> {
        return dao.getRssiStats(deviceAddress)
    }
}