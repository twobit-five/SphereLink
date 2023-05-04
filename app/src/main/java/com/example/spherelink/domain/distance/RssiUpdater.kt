package com.example.spherelink.domain.distance

import com.example.spherelink.data.entities.DeviceRssiHistory
import com.example.spherelink.domain.repo.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEVICE_HISTORY_LIMIT = 50
private const val RSSI_STANDARD_DEVIATION_LIMIT = 2
private const val RSSI_TIME_THRESHOLD = 5000L

class RssiUpdater  @Inject constructor(private val repository: DeviceRepository) {
    fun updateRssi(deviceAddress: String, currentRSSI: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            // TODO this needs to called from device manager or bluetooth service
            // since this only gets called when the onreadrssi is called
            // if the device is not connected, this will never get called.
            removeStaleValues(deviceAddress)

            //add new rssi value to database blindly
            repository.insertRssiValueWithLimit(
                DeviceRssiHistory(
                    timestamp = System.currentTimeMillis(),
                    deviceAddress = deviceAddress,
                    rssi =  currentRSSI), DEVICE_HISTORY_LIMIT)

            repository.updateDeviceEntityRssi(deviceAddress, currentRSSI)
        }
    }

    fun removeStaleValues(deviceAddress: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //clear old rssi values with threshold
            repository.deleteOldRssiValues(deviceAddress, System.currentTimeMillis() - RSSI_TIME_THRESHOLD)
        }
    }
}

